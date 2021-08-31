package pl.pfranczak.j2bills2.monolith.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.Alerts;

@Repository
public interface AlertsRepository extends CrudRepository<Alerts, Long>{
	List<Alerts> findByOwner(UserAccount owner);
	Alerts findByIdAndOwner(Long id, UserAccount owner);
}
