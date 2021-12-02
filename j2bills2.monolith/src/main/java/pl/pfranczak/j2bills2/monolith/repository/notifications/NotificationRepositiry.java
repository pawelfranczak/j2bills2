package pl.pfranczak.j2bills2.monolith.repository.notifications;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notification;

@Repository
public interface NotificationRepositiry extends CrudRepository<Notification, Long> {
	List<Notification> findByOwner(UserAccount owner);
	List<Notification> findByOwnerAndActiveTrueOrderByDateDesc(UserAccount owner);
	List<Notification> findByOwnerAndActiveFalseOrderByDateDesc(UserAccount owner);
	Notification findByIdAndOwner(Long id, UserAccount owner);
	long countByActiveTrue();
}
