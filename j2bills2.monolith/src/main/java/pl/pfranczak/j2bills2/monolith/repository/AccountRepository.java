package pl.pfranczak.j2bills2.monolith.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.User;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	
}
