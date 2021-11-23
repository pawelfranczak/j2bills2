package pl.pfranczak.j2bills2.monolith.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.notification.Notification;
import pl.pfranczak.j2bills2.monolith.repository.notifications.NotificationRepositiry;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class NotificationService extends CrudServiceImpl<Notification, Long>{

	NotificationRepositiry notificationRepository;
	
	public NotificationService(NotificationRepositiry notificationRepository) {
		super.setRepository(notificationRepository);
		this.notificationRepository = notificationRepository;
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
		
	}
	
}
