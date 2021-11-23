package pl.pfranczak.j2bills2.monolith.repository.bills;

import java.time.Month;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;

public interface BillsOfMonthRepository extends CrudRepository<BillsOfMonth, Long>{
	
	List<BillsOfMonth> findByOwner(UserAccount owner);
	BillsOfMonth findByIdAndOwner(Long id, UserAccount owner);
	List<BillsOfMonth> findByOwnerAndYearAndMonth(UserAccount owner, Long year, Month month);
	List<BillsOfMonth> findByOwnerAndPaidFalse(UserAccount owner);
}
