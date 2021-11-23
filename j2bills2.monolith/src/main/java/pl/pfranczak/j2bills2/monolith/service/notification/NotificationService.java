package pl.pfranczak.j2bills2.monolith.service.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notification;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notified;
import pl.pfranczak.j2bills2.monolith.repository.bills.BillsOfMonthRepository;
import pl.pfranczak.j2bills2.monolith.repository.notifications.NotificationRepositiry;
import pl.pfranczak.j2bills2.monolith.repository.notifications.NotifiedRepositiry;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class NotificationService extends CrudServiceImpl<Notification, Long>{

	NotificationRepositiry notificationRepository;
	BillsOfMonthRepository billsOfMonthRepository;
	NotifiedRepositiry notifiedRepositiry;
	
	public NotificationService(NotificationRepositiry notificationRepository, BillsOfMonthRepository billsOfMonthRepository, NotifiedRepositiry notifiedRepositiry) {
		super.setRepository(notificationRepository);
		this.notificationRepository = notificationRepository;
		this.billsOfMonthRepository = billsOfMonthRepository;
		this.notifiedRepositiry = notifiedRepositiry;
	}
	
	@Override
	public List<Notification> getAll() {
		return notificationRepository.findByOwner(getOwner());
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
		List<BillsOfMonth> billsOfMonth = billsOfMonthRepository.findByOwnerAndPaidFalse(getOwner());
		for (BillsOfMonth billOfMonth : billsOfMonth) {
			Optional<Notified> notified = notifiedRepositiry.findById(billOfMonth.getId());
			if (notified.isEmpty()) {
				// generate 1st notification about outstanding bill
				// Rachunek Y na kwotę X ma termin płatności Z 
				// TODO
			}
		}
	}
	
}
