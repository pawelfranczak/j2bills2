package pl.pfranczak.j2bills2.monolith.service.bills;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;
import pl.pfranczak.j2bills2.monolith.repository.bills.BillRepository;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class BillService extends CrudServiceImpl<Bill, Long>{
	
	BillRepository billRepository;

	public BillService(BillRepository billRepository) {
		super.setRepository(billRepository);
		this.billRepository = billRepository;
	}
	
	@Override
	public Bill get(Long id) {
		return billRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public List<Bill> getAll() {
		return billRepository.findByOwner(getOwner());
	}
	
	@Override
	public void create(Bill bill) {
		bill.setOwner(getOwner());
		super.create(bill);
	}
}
