package pl.pfranczak.j2bills2.monolith.service.notification;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notification;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notified;
import pl.pfranczak.j2bills2.monolith.repository.notifications.NotificationRepositiry;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;
import pl.pfranczak.j2bills2.monolith.service.bills.BillsOfMonthService;

@Service
public class NotificationService extends CrudServiceImpl<Notification, Long>{

	NotificationRepositiry notificationRepository;
	BillsOfMonthService billsOfMonthService;
	NotifiedService notifiedService;
	
	public NotificationService(NotificationRepositiry notificationRepository,BillsOfMonthService billsOfMonthService, NotifiedService notifiedService) {
		super.setRepository(notificationRepository);
		this.notificationRepository = notificationRepository;
		this.billsOfMonthService = billsOfMonthService;
		this.notifiedService = notifiedService;
	}
	
	@Override
	public List<Notification> getAll() {
		return notificationRepository.findByOwnerAndActiveTrueOrderByDateDesc(getOwner());
	}
	
	public List<Notification> getAllNotActive() {
		return notificationRepository.findByOwnerAndActiveFalseOrderByDateDesc(getOwner());
	}
	
	@Override
	public Notification get(Long id) {
		return notificationRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(Notification notification) {
		notification.setOwner(getOwner());
		super.create(notification);
	}
	
	@Override
	public void update(Notification notification) {
		notification.setOwner(getOwner());
		super.update(notification);
	}
	
	public void generateNotification() {
		List<BillsOfMonth> billsOfMonth = billsOfMonthService.getByOwnerAndAndPaid(false);
		for (BillsOfMonth billOfMonth : billsOfMonth) {
			Notified notified = notifiedService.get(billOfMonth.getId());
			int daysToDueDate = daysToDueDate(billOfMonth);
			if (notificationShouldBeGenerated(notified, daysToDueDate)) {
				if (billIsToPayInFuture(billOfMonth)) {
					createNotificationToPayInFuture(billOfMonth);
				} else if (billIsToPayToday(billOfMonth)) {
					createNotificationToPayToday(billOfMonth);
				}
			}
		}
	}
	
	private boolean notificationShouldBeGenerated(Notified notified, int daysToDueDate) {
		// 7 3 1 0, magic numbers -> todo move to DB
		int daysBetweenLastGenerationAndNow = Integer.MAX_VALUE;
		if (notified != null) {
			daysBetweenLastGenerationAndNow = calcDaysBetween(notified.getTimestamp().toLocalDateTime().toLocalDate());
			// value is less than zero (it is past)
			daysBetweenLastGenerationAndNow = daysBetweenLastGenerationAndNow * -1;
		}
		if ((daysToDueDate == 7 && (notified == null || daysBetweenLastGenerationAndNow > 7)) ||
			(daysToDueDate == 3 && (notified == null || daysBetweenLastGenerationAndNow > 3)) ||
			(daysToDueDate == 1 && (notified == null || daysBetweenLastGenerationAndNow > 1)) ||
			(daysToDueDate == 0 && (notified == null || daysBetweenLastGenerationAndNow > 0))) {
			return true;
		}
		
		return false;
	}

	private void createNotificationToPayToday(BillsOfMonth billOfMonth) {
		Notification notification = new Notification();
		
		String notificationString = "Dzisiaj ma termin płatności rachunek \"" + billOfMonth.getName() + "\" na kwotę " + billOfMonth.getAmount();
		
		notification.setActive(true);
		notification.setDate(getTimestamp());
		notification.setNotification(notificationString);
		
		this.create(notification);
		
		Notified notified = new Notified(billOfMonth.getId(), getTimestamp());
		notifiedService.create(notified);
	}

	private void createNotificationToPayInFuture(BillsOfMonth billOfMonth) {
		
		Notification notification = new Notification();
		
		int daysToDueDate = daysToDueDate(billOfMonth);
		String dueDate = billOfMonth.getYear() + "." + billOfMonth.getMonth().getValue() + "." + billOfMonth.getDueDay();
		String notificationString = "Rachunek \"" + billOfMonth.getName() + "\" na kwotę " + billOfMonth.getAmount() + " ma termin płatności " + dueDate + ". Pozostało " + daysToDueDate + " dni.";
		
		notification.setActive(true);
		notification.setDate(getTimestamp());
		notification.setNotification(notificationString);
		
		this.create(notification);
		
		Notified notified = new Notified(billOfMonth.getId(), getTimestamp());
		notifiedService.create(notified);
	}
	
	private int daysToDueDate(BillsOfMonth billOfMonth) {
		if (billIsToPayToday(billOfMonth)) {
			return 0;	
		} else {
			return calcDaysBetweenBillDueDateAndNow(billOfMonth);
		}
	}

	private int calcDaysBetweenBillDueDateAndNow(BillsOfMonth billOfMonth) {
		LocalDate billDueDate = LocalDate.of(billOfMonth.getYear().intValue(), billOfMonth.getMonth(), billOfMonth.getDueDay().intValue());
		return (int)calcDaysBetween(billDueDate);
	}
	
	private int calcDaysBetween(LocalDate billDueDate) {
		LocalDate currentdate = LocalDate.now();
		return (int)currentdate.until(billDueDate, ChronoUnit.DAYS);
	}

	private boolean billIsToPayInFuture(BillsOfMonth billOfMonth) {
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		int currentYear = currentdate.getYear();
		int currentDay = currentdate.getDayOfMonth();
		
		if (billOfMonth.getYear().intValue() > currentYear) {
			return true;
		} else if (billOfMonth.getYear().intValue() == currentYear ) {
			if (billOfMonth.getMonth().getValue() > currentMonth.getValue()) {
				return true;
			} else if (billOfMonth.getMonth().getValue() == currentMonth.getValue()) {
				if (billOfMonth.getDueDay() > currentDay) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean billIsToPayToday(BillsOfMonth billOfMonth) {
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		int currentYear = currentdate.getYear();
		int currentDay = currentdate.getDayOfMonth();
		
		if (currentYear == billOfMonth.getYear() && currentMonth.getValue() == billOfMonth.getMonth().getValue() && currentDay == billOfMonth.getDueDay()) {
			return true;
		}
		
		return false;
	}
	
}
