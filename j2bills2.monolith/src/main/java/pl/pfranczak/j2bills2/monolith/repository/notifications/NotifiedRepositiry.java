package pl.pfranczak.j2bills2.monolith.repository.notifications;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.entity.notification.Notified;

@Repository
public interface NotifiedRepositiry extends CrudRepository<Notified, Long> {

}
