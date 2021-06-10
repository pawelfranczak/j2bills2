package pl.pfranczak.j2bills2.monolith.service.bills;

import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.repository.bills.BillsOfMonthRepository;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;
import pl.pfranczak.j2bills2.monolith.service.JournalService;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;

@Service
public class BillsOfMonthService extends CrudServiceImpl<BillsOfMonth, Long>{
	
	BillsOfMonthRepository billRepository;
	JournalService journalService;
	UserSettingsService userSettingsService;
	UserService userService;


	public BillsOfMonthService(BillsOfMonthRepository billRepository, JournalService journalService, UserSettingsService userSettingsService, UserService userService) {
		super.setRepository(billRepository);
		this.journalService = journalService;
		this.userSettingsService = userSettingsService;
		this.billRepository = billRepository;
		this.userService = userService;
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
		bill.setPaid(false);
		super.create(bill);
	}
	
	public boolean payBill(BillsOfMonth billsOfMonth) {
		Account billsAccount = userSettingsService.getBillsAccount();
		if (billsAccount == null) {
			return false;
		}
		
		Journal journal = new Journal();
		journal.setAccount(billsAccount);
		journal.setDescription(billsOfMonth.getDescription());
		journal.setValue(billsOfMonth.getAmount().negate());
		journal.setUser(userService.getAll().get(0));
		journalService.create(journal);
		
		billsOfMonth.setPaid(true);
		billRepository.save(billsOfMonth);
		
		return true;
	}
	
}
