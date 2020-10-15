package pl.pfranczak.j2bills2.monolith.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	List<User> findByOwner(UserAccount owner);
	User findByIdAndOwner(Long id, UserAccount owner);
}
