package pl.pfranczak.j2bills2.monolith.service.bills;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.entity.bills.CopyMonth;
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
	
	public List<BillsOfMonth> getByOwnerAndPaidAndAutomaticRepayment(boolean paid, boolean automaticRepeyment) {
			return billRepository.findByOwnerAndPaidAndAutomaticRepayment(getOwner(), paid, automaticRepeyment);
	}
	
	@Override
	public void create(BillsOfMonth bill) {
		bill.setOwner(getOwner());
		bill.setPaid(false);
		super.create(bill);
	}
	
	public boolean payBill(BillsOfMonth billsOfMonth, BigDecimal originalBillValue, BigDecimal newBillValue) {
		Account billsAccount = userSettingsService.getBillsAccount();
		if (billsAccount == null) {
			return false;
		}
		
		Account billsDifferenceAccount = userSettingsService.getBillsDifferenceAccount();
		if (billsDifferenceAccount != null && originalBillValue != null && newBillValue != null) {
			Journal journal = new Journal();
			journal.setAccount(billsAccount);
			journal.setDescription(billsOfMonth.getName());
			journal.setValue(originalBillValue.negate());
			journal.setUser(userService.getAll().get(0));
			journalService.create(journal);
			
			BigDecimal difference = newBillValue.subtract(originalBillValue);
			if (difference.compareTo(BigDecimal.ZERO) != 0) {
				Journal journalDifference = new Journal();
				journalDifference.setAccount(billsDifferenceAccount);
				journalDifference.setDescription(billsOfMonth.getName());
				journalDifference.setValue(difference.negate());
				journalDifference.setUser(userService.getAll().get(0));
				journalService.create(journalDifference);
			}
		} else {
			Journal journal = new Journal();
			journal.setAccount(billsAccount);
			journal.setDescription(billsOfMonth.getName());
			journal.setValue(billsOfMonth.getAmount().negate());
			journal.setUser(userService.getAll().get(0));
			journalService.create(journal);
		}
		
		billsOfMonth.setPaid(true);
		billRepository.save(billsOfMonth);
	
		return true;
	}
	
	public void copyMonth(CopyMonth copyMonth) {
		
		List<BillsOfMonth> billsOfMonths = getByOwnerAndYearAndMonth(copyMonth.getSourceYear(), copyMonth.getSourceMonth());
		for (BillsOfMonth oldBillsOfMonth : billsOfMonths) {
			BillsOfMonth newBillsOfMonth = new BillsOfMonth();
			newBillsOfMonth.setOwner(getOwner());
			newBillsOfMonth.setName(oldBillsOfMonth.getName());
			newBillsOfMonth.setDescription(oldBillsOfMonth.getDescription());
			newBillsOfMonth.setMonth(Month.of(copyMonth.getTargetMonth().intValue()));
			newBillsOfMonth.setYear(copyMonth.getTargerYear());
			newBillsOfMonth.setAmount(oldBillsOfMonth.getAmount());
			newBillsOfMonth.setAmountPaid(BigDecimal.ZERO);
			newBillsOfMonth.setDueDay(oldBillsOfMonth.getDueDay());
			newBillsOfMonth.setPaid(Boolean.FALSE);
			newBillsOfMonth.setAutomaticRepayment(oldBillsOfMonth.getAutomaticRepayment());
			create(newBillsOfMonth);
		}
		
	}
	
	@Override
	public void update(BillsOfMonth entity) {
		entity.setOwner(getOwner());
		super.update(entity);
	}

	public void payOutstandingAutomaticRepaymentBills() {
		// system should pay for every automatic repayments bills in the past
		List<BillsOfMonth> billsOfMonth = billRepository.findByOwnerAndPaidAndAutomaticRepayment(getOwner(), false, true);
		for (BillsOfMonth billOfMonth : billsOfMonth) {
			if (canBeAutomaticRepayed(billOfMonth)) {
				payBill(billOfMonth, null, null);
			}
		}
	}

	private boolean canBeAutomaticRepayed(BillsOfMonth billOfMonth) {
		LocalDate currentdate = LocalDate.now();
		int currentYear = currentdate.getYear();
		int currentMonth = currentdate.getMonth().getValue();
		int currentDay = currentdate.getDayOfMonth();
		
		int billYear = billOfMonth.getYear().intValue();
		int billMonth = billOfMonth.getMonth().getValue();
		int billDay = billOfMonth.getDueDay();
		
		boolean billInPast = false;
		
		if (currentYear > billYear) billInPast = true;
		if (currentYear == billYear && currentMonth > billMonth) billInPast = true;
		if (currentYear == billYear && currentMonth == billMonth && currentDay > billDay) billInPast = true;
		
		return billInPast;
	}
	
}
