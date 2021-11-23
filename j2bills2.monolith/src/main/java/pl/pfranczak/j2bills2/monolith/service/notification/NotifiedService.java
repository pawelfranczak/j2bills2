package pl.pfranczak.j2bills2.monolith.service.notification;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.notification.Notified;
import pl.pfranczak.j2bills2.monolith.repository.notifications.NotifiedRepositiry;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class NotifiedService extends CrudServiceImpl<Notified, Long>{

	NotifiedRepositiry notifiedRepositiry;
	
	public NotifiedService(NotifiedRepositiry notifiedRepositiry) {
		super.setRepository(notifiedRepositiry);
		this.notifiedRepositiry = notifiedRepositiry;
	}
	
}
