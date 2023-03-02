package pl.pfranczak.j2bills2.monolith.controller.bills;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;
import pl.pfranczak.j2bills2.monolith.entity.bills.BillsOfMonth;
import pl.pfranczak.j2bills2.monolith.entity.bills.CopyMonth;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillsOfMonthService;
import pl.pfranczak.j2bills2.monolith.service.category.CategoryService;
import pl.pfranczak.j2bills2.monolith.service.category.SubCategoryService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
@RequestMapping("${bill}")
public class BillController {
	
	private BillService billService; 
	private UserService userService; 
	private BillsOfMonthService billsOfMonthService;
	private UserSettingsService userSettingsService;
	private NotificationService notificationService;
	private CategoryService categoryService;
	private SubCategoryService subCategoryService;
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("bill/all");
		List<Bill> bills = billService.getAll();
		modelAndView.addObject("bills", bills);
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("bill/one");
		Bill bill = billService.get(id);
		modelAndView.addObject("bill", bill);
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Bill bill) {
		ModelAndView modelAndView = new ModelAndView("bill/new");
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Bill bill, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("bill/new");
			userService.addUsernameToModelAndView(modelAndView);
			long countOfActiveNotification = notificationService.getCountOfActiveNotification();
			modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
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
		
		List<Category> categories = categoryService.getAllActive();
		modelAndView.addObject("categories", categories);
		List<SubCategory> subCategories = subCategoryService.getAllWithActiveCategory();
		modelAndView.addObject("subCategories", subCategories);
		
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
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
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		int currentYear = currentdate.getYear();
		return new ModelAndView("redirect:/bill/show_by_month/" + currentYear + "/" + currentMonth.getValue());
	}
	
	@GetMapping("${show_by_year}")	
	public ModelAndView showAllYear() {
		LocalDate currentdate = LocalDate.now();
		int currentYear = currentdate.getYear();
		return new ModelAndView("redirect:/bill/show_by_year/" + currentYear);
	}
	
	@GetMapping("${show_by_year}/{year}")	
	public ModelAndView showAllByYear(@PathVariable("year") Long year) {
		ModelAndView modelAndView = new ModelAndView("bill/show_by_year");
		
		List<List<BillsOfMonth>> billsOfYear = new ArrayList<>();
		BigDecimal toPayYear = BigDecimal.ZERO;
		BigDecimal paidYear = BigDecimal.ZERO;

		for (Month month : Month.values()) {
			List<BillsOfMonth> billsOfMonths = billsOfMonthService.getByOwnerAndYearAndMonth(year, (long)month.getValue());
			if (billsOfMonths.size() > 0) {
				billsOfYear.add(billsOfMonths);
				paidYear = billsOfMonths.stream()	
						.map(BillsOfMonth::getAmountPaid)
						.filter(Objects::nonNull)
						.reduce(paidYear, BigDecimal::add);
				toPayYear = billsOfMonths.stream()	
						.map(BillsOfMonth::getAmount)
						.filter(Objects::nonNull)
						.reduce(toPayYear, BigDecimal::add);
			}
		}
		
		
		modelAndView.addObject("toPayYear", toPayYear);
		modelAndView.addObject("paidYear", paidYear);
		modelAndView.addObject("billsOfYear", billsOfYear);
		modelAndView.addObject("nextYear", "/" + (year+1));
		modelAndView.addObject("previousYear", "/" + (year-1));
		
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@GetMapping("${show_by_month}/{year}/{month}")	
	public ModelAndView showAllByMonth(@PathVariable("year") Long year, @PathVariable("month") Long month) {
		
		billsOfMonthService.payOutstandingAutomaticRepaymentBills();
		
		ModelAndView modelAndView = new ModelAndView("bill/show_by_month");
		List<BillsOfMonth> billsOfMonths = billsOfMonthService.getByOwnerAndYearAndMonth(year, month);
		modelAndView.addObject("billsOfMonths", billsOfMonths);
		modelAndView.addObject("nextMonth", generateNextMonthLink(month, year));
		modelAndView.addObject("previousMonth", generatePreviousMonthLink(month, year));
		modelAndView.addObject("sumOfMonth", calculateBillsSumOfMonth(billsOfMonths));
		modelAndView.addObject("sumOfMonthToPay", calculateBillsSumOfMonthToPay(billsOfMonths));
		modelAndView.addObject("sumOfMonthPaid", calculateBillsSumOfMonthPaid(billsOfMonths));
		
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
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
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
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
		if (billsOfMonth.getCategory() != null) {
			modelAndView.addObject("categoryID", billsOfMonth.getCategory().getId());
		}
		if (billsOfMonth.getSubCategory() != null) {
			modelAndView.addObject("subCategoryID", billsOfMonth.getSubCategory().getId());
		}
		
		List<Category> categories = categoryService.getAllActive();
		modelAndView.addObject("categories", categories);
		List<SubCategory> subCategories = subCategoryService.getAllWithActiveCategory();
		modelAndView.addObject("subCategories", subCategories);
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
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
//		billOfMonth.setPaid(false);
//		billOfMonth.setAutomaticRepayment(billsOfMonthService.get(billOfMonth.getId()).getAutomaticRepayment());
		billsOfMonthService.update(billOfMonth);
		billsOfMonthService.payBill(billOfMonth, originalBillValue, newBillValue);	
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@GetMapping("${modify}/{id}")
	public ModelAndView modifyGet(@PathVariable("id") Long id) {
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
		
		ModelAndView modelAndView = new ModelAndView("bill/modify");
		modelAndView.addObject("billsOfMonth", billsOfMonth);
		modelAndView.addObject("accountID", billsAccount.getId());
		modelAndView.addObject("monthSelected", billsOfMonth.getMonth());
		if (billsOfMonth.getCategory() != null) {
			modelAndView.addObject("categoryID", billsOfMonth.getCategory().getId());
		}
		if (billsOfMonth.getSubCategory() != null) {
			modelAndView.addObject("subCategoryID", billsOfMonth.getSubCategory().getId());
		}
		
		List<Category> categories = categoryService.getAllActive();
		modelAndView.addObject("categories", categories);
		List<SubCategory> subCategories = subCategoryService.getAllWithActiveCategory();
		modelAndView.addObject("subCategories", subCategories);
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@PostMapping("${modify}")
	public ModelAndView modifyPost(@Valid BillsOfMonth billOfMonth, BindingResult bindingResult) {
		Long year = billOfMonth.getYear();
		int month = billOfMonth.getMonth().getValue();
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		billOfMonth.setPaid(false);
		billOfMonth.setAmountPaid(BigDecimal.ZERO);
		billsOfMonthService.update(billOfMonth);
		return modelAndView;
	}
	
	@GetMapping("${delete}/{year}/{month}/{id}")
	public ModelAndView deleteGet(@PathVariable("year") Long year, @PathVariable("month") Long month, @PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/show_by_month/" + year + "/" + month);
		billsOfMonthService.deleteById(id);
		return modelAndView;
	}
	
	@GetMapping("${copy_month}")
	public ModelAndView copyMonthGet() {
		LocalDate currentdate = LocalDate.now();
		Month currentMonthEnum = currentdate.getMonth();
		int currentMonth = currentMonthEnum.getValue();
		int currentYear = currentdate.getYear();
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/copy_month/" + currentYear + "/" + currentMonth + "/" + currentYear + "/" + currentMonth);
		return modelAndView;
	}
	
	@GetMapping("${copy_month}/{sourceYear}/{sourceMonth}/{targerYear}/{targetMonth}")
	public ModelAndView copyMonthWithAtributesGet(@PathVariable("sourceYear") Long sourceYear, @PathVariable("sourceMonth") Long sourceMonth, @PathVariable("targerYear") Long targerYear, @PathVariable("targetMonth") Long targetMonth) {
		ModelAndView modelAndView = new ModelAndView("bill/copy_month");
		CopyMonth copyMonth = new CopyMonth(sourceYear, sourceMonth, targerYear, targetMonth);
		modelAndView.addObject("copyMonth", copyMonth);
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@PostMapping("${copy_month}")
	public ModelAndView copyMonthPost(@Valid CopyMonth copyMonth, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("redirect:/bill/show_by_month/" + copyMonth.getTargerYear() + "/" + copyMonth.getTargetMonth());
		billsOfMonthService.copyMonth(copyMonth);
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

}
