package pl.pfranczak.j2bills2.monolith.service.bills;

import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.repository.bills.BillsOfMonthRepository;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class BillsOfMonthService extends CrudServiceImpl<BillsOfMonth, Long>{
	
	BillsOfMonthRepository billRepository;

	public BillsOfMonthService(BillsOfMonthRepository billRepository) {
		super.setRepository(billRepository);
		this.billRepository = billRepository;
	}
	
	@Override
	public BillsOfMonth get(Long id) {
		return billRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public List<BillsOfMonth> getAll() {
		return billRepository.findByOwner(getOwner());
	}
	
	public List<BillsOfMonth> getByOwnerAndYearAndMonth(Long year, Long month) {
		return billRepository.findByOwnerAndYearAndMonth(getOwner(), year, Month.of(month.intValue()));
	}
	
	@Override
	public void create(BillsOfMonth bill) {
		bill.setOwner(getOwner());
		super.create(bill);
	}
}
