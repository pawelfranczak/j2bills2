package pl.pfranczak.j2bills2.monolith.controller.bills;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillsOfMonthService;

@AllArgsConstructor
@Controller
@RequestMapping("${bill}")
public class BillController {
	
	private BillService billService; 
	private UserService userService; 
	private BillsOfMonthService billsOfMonthService;
	private UserSettingsService userSettingsService;
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("bill/all");
		List<Bill> bills = billService.getAll();
		modelAndView.addObject("bills", bills);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("bill/one");
		Bill bill = billService.get(id);
		modelAndView.addObject("bill", bill);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Bill bill) {
		ModelAndView modelAndView = new ModelAndView("bill/new");
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Bill bill, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("bill/new");
			userService.addUsernameToModelAndView(modelAndView);
			return modelAndView;
		}
		billService.create(bill);
		return new ModelAndView("redirect:/bill/all");
	}
	
	@GetMapping("${add_to_month}")
	public ModelAndView addToMonth(BillsOfMonth billsOfMonth) {
		ModelAndView modelAndView = new ModelAndView("bill/add_to_month");
		
		List<Bill> bills = billService.getAll();
		List<BigDecimal> defaultAmounts = new ArrayList<BigDecimal>();
		List<Byte> defaultDueDays = new ArrayList<Byte>();
		List<String> defaultNames = new ArrayList<String>();
		List<String> defaultDescriptions = new ArrayList<String>();
		
		defaultAmounts = bills.stream().map(Bill::getDefaultAmount).collect(Collectors.toList());
		defaultDueDays = bills.stream().map(Bill::getDefaultDueDay).collect(Collectors.toList());
		defaultNames = bills.stream().map(Bill::getName).collect(Collectors.toList());
		defaultDescriptions = bills.stream().map(Bill::getDescription).collect(Collectors.toList());
		
		modelAndView.addObject("bills", bills);
		modelAndView.addObject("defaultAmounts", defaultAmounts);
		modelAndView.addObject("defaultDueDays", defaultDueDays);
		modelAndView.addObject("defaultNames", defaultNames);
		modelAndView.addObject("defaultDescriptions", defaultDescriptions);
		
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${add_to_month}")
	public ModelAndView addToMonthPost(@Valid BillsOfMonth billsOfMonth, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("bill/add_to_month");
			
			List<Bill> bills = billService.getAll();
			List<BigDecimal> defaultAmounts = new ArrayList<BigDecimal>();
			List<Byte> defaultDueDays = new ArrayList<Byte>();
			List<String> defaultNames = new ArrayList<String>();
			List<String> defaultDescriptions = new ArrayList<String>();
			
			defaultAmounts = bills.stream().map(Bill::getDefaultAmount).collect(Collectors.toList());
			defaultDueDays = bills.stream().map(Bill::getDefaultDueDay).collect(Collectors.toList());
			defaultNames = bills.stream().map(Bill::getName).collect(Collectors.toList());
			defaultDescriptions = bills.stream().map(Bill::getDescription).collect(Collectors.toList());
			
			modelAndView.addObject("bills", bills);
			modelAndView.addObject("defaultAmounts", defaultAmounts);
			modelAndView.addObject("defaultDueDays", defaultDueDays);
			modelAndView.addObject("defaultNames", defaultNames);
			modelAndView.addObject("defaultDescriptions", defaultDescriptions);
			
			userService.addUsernameToModelAndView(modelAndView);
			return modelAndView;
		}
		billsOfMonthService.create(billsOfMonth);
		return new ModelAndView("redirect:/bill/add_to_month");
	}
	
	@GetMapping("${show_by_month}")	
	public ModelAndView showAllByMonth() {
		return new ModelAndView("redirect:/bill/show_by_month/2021/6");
	}
	
	@GetMapping("${show_by_month}/{year}/{month}")	
	public ModelAndView showAllByMonth(@PathVariable("year") Long year, @PathVariable("month") Long month) {
		ModelAndView modelAndView = new ModelAndView("bill/show_by_month");
		List<BillsOfMonth> billsOfMonths = billsOfMonthService.getByOwnerAndYearAndMonth(year, month);
		modelAndView.addObject("billsOfMonths", billsOfMonths);
		modelAndView.addObject("nextMonth", generateNextMonthLink(month, year));
		modelAndView.addObject("previousMonth", generatePreviousMonthLink(month, year));
		modelAndView.addObject("sumOfMonth", calculateBillsSumOfMonth(billsOfMonths));
		modelAndView.addObject("sumOfMonthToPay", calculateBillsSumOfMonthToPay(billsOfMonths));
		modelAndView.addObject("sumOfMonthPaid", calculateBillsSumOfMonthPaid(billsOfMonths));
		
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	private BigDecimal calculateBillsSumOfMonth(List<BillsOfMonth> billsOfMonths) {
		BigDecimal sumOfMonth = BigDecimal.ZERO;
		for (BillsOfMonth billsOfMonth : billsOfMonths) {
			sumOfMonth = sumOfMonth.add(billsOfMonth.getAmount());
		}
		return sumOfMonth;
	}
	
	private BigDecimal calculateBillsSumOfMonthToPay(List<BillsOfMonth> billsOfMonths) {
		BigDecimal sumOfMonth = BigDecimal.ZERO;
		for (BillsOfMonth billsOfMonth : billsOfMonths) {
			if (!billsOfMonth.getPaid())
				sumOfMonth = sumOfMonth.add(billsOfMonth.getAmount());
		}
		return sumOfMonth;
	}
	
	private BigDecimal calculateBillsSumOfMonthPaid(List<BillsOfMonth> billsOfMonths) {
		BigDecimal sumOfMonth = BigDecimal.ZERO;
		for (BillsOfMonth billsOfMonth : billsOfMonths) {
			if (billsOfMonth.getPaid())
				sumOfMonth = sumOfMonth.add(billsOfMonth.getAmount());
		}
		return sumOfMonth;
	}

	@GetMapping("${pay}/{id}")
	public ModelAndView pay(@PathVariable("id") Long id) {
		BillsOfMonth billOfMonth = billsOfMonthService.get(id);
		Long year = billOfMonth.getYear();
		int month = billOfMonth.getMonth().getValue();
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		if (billOfMonth.getPaid()) {
			return modelAndView;
		}
		billOfMonth.setAmountPaid(billOfMonth.getAmount());
		billsOfMonthService.update(billOfMonth);
		billsOfMonthService.payBill(billOfMonth, null, null);		
		return modelAndView;
	}
	
	@GetMapping("${edit_and_pay}/{id}")
	public ModelAndView editAndPayGet(@PathVariable("id") Long id) {
		BillsOfMonth billsOfMonth = billsOfMonthService.get(id);
		Long year = billsOfMonth.getYear();
		int month = billsOfMonth.getMonth().getValue();
		if (billsOfMonth.getPaid()) {
			return new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		}
		
		Account billsAccount = userSettingsService.getBillsAccount();
		if (billsAccount == null) {
			return new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		}
		
		ModelAndView modelAndView = new ModelAndView("bill/edit_and_pay");
		modelAndView.addObject("billsOfMonth", billsOfMonth);
		modelAndView.addObject("accountID", billsAccount.getId());
		modelAndView.addObject("monthSelected", billsOfMonth.getMonth());
		
		return modelAndView;
	}
	
	@PostMapping("${edit_and_pay}")
	public ModelAndView editAndPayPost(@Valid BillsOfMonth billOfMonth, BindingResult bindingResult) {
		Long year = billOfMonth.getYear();
		int month = billOfMonth.getMonth().getValue();
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		BigDecimal originalBillValue = billsOfMonthService.get(billOfMonth.getId()).getAmount();
		BigDecimal newBillValue = billOfMonth.getAmount();
		billOfMonth.setAmountPaid(newBillValue);
		billOfMonth.setAmount(originalBillValue);
		billOfMonth.setPaid(true);
		billsOfMonthService.update(billOfMonth);
		billsOfMonthService.payBill(billOfMonth, originalBillValue, newBillValue);		
		return modelAndView;
	}
	
	private String generateNextMonthLink(Long month, Long year) {
		
		Long nextMonth;
		Long nextYear;
		
		if (month == 12) {
			nextMonth = 1L;
			nextYear = year + 1L;
		} else {
			nextMonth = month + 1L;
			nextYear = year;
		}
		
		return "/" + nextYear + "/" + nextMonth;
		
	}
	
	private String generatePreviousMonthLink(Long month, Long year) {
		
		Long previousMonth;
		Long previousYear;
		
		if (month == 1) {
			previousMonth = 12L;
			previousYear = year - 1L;
		} else {
			previousMonth = month - 1L;
			previousYear = year;
		}
		
		return "/" + previousYear + "/" + previousMonth;
		
	}
	
	
//	@GetMapping("${modify}/{id}")
//	public ModelAndView modifywEntity(@PathVariable("id") Long id) {
//		ModelAndView modelAndView = new ModelAndView("account/modify");
//		Account account = accountService.get(id);
//		modelAndView.addObject("account", account);
//		List<User> users = userService.getAll();
//		modelAndView.addObject("users", users);
//		userService.addUsernameToModelAndView(modelAndView);
//		return modelAndView;
//	}
//
//	// TODO Add validation
//	@PostMapping("${modify}")
//	public String modifyEntityPost(Account account) {
//		accountService.update(account);
//		return "redirect:/account/all";
//	}

}
