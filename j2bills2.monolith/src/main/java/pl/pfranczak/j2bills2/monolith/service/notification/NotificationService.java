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
			if (notified == null) {
				// generate 1st notification about outstanding bill
				// Rachunek Y na kwotę X ma termin płatności Z 
				if (billIsToPayInFuture(billOfMonth)) {
					createNotificationToPayInFuture(billOfMonth);
				} else if (billIsToPayToday(billOfMonth)) {
					createNotificationToPayToday(billOfMonth);
				}
			}
		}
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
		String notificationString = "Rachunek \"" + billOfMonth.getName() + "\" na kwotę " + billOfMonth.getAmount() + " ma termin płatności " + dueDate + ". Pozostało " + "Pozostało " + daysToDueDate + " dni.";
		
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
			return calcDaysBetween(billOfMonth);
		}
	}

	private int calcDaysBetween(BillsOfMonth billOfMonth) {
		LocalDate currentdate = LocalDate.now();
		LocalDate billDueDate = LocalDate.of(billOfMonth.getYear().intValue(), billOfMonth.getMonth(), billOfMonth.getDueDay().intValue());
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
