package pl.pfranczak.j2bills2.monolith.repository.bills;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;

public interface BillRepository extends CrudRepository<Bill, Long>{
	
	List<Bill> findByOwner(UserAccount owner);
	Bill findByIdAndOwner(Long id, UserAccount owner);

}
