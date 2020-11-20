package pl.pfranczak.j2bills2.monolith.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	List<Account> findByOwner(UserAccount owner);
	List<Account> findByOwnerAndActiveTrue(UserAccount owner);
	Account findByIdAndOwner(Long id, UserAccount owner);
}
