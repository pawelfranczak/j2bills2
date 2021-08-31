package pl.pfranczak.j2bills2.monolith.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.AlertsGenerationByDay;

@Repository
public interface AlertsGenerationByDayRepository extends CrudRepository<AlertsGenerationByDay, Long>{
	List<AlertsGenerationByDay> findByOwner(UserAccount owner);
	AlertsGenerationByDay findByIdAndOwner(Long id, UserAccount owner);
	AlertsGenerationByDay findByOwnerAndDay(UserAccount owner, LocalDate day);
}
