package pl.pfranczak.j2bills2.monolith.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	
}
