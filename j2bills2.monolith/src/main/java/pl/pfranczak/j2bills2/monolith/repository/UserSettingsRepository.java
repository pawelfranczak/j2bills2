package pl.pfranczak.j2bills2.monolith.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;

@Repository
public interface UserSettingsRepository extends CrudRepository<UserSettings, Long>{
	List<UserSettings> findByOwner(UserAccount owner);
}
