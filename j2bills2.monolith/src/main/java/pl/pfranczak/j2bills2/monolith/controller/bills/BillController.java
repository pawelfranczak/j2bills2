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
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillsOfMonthService;

@AllArgsConstructor
@Controller
@RequestMapping("${bill}")
public class BillController {
	
	private BillService billService; 
	private UserService userService; 
	private BillsOfMonthService billsOfMonthService;
	
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
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
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
