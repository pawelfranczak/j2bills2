package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillsOfMonthService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
public class IndexController {
	
	AccountService accountService;
	UserService userService;
	UserSettingsService userSettingsService;
	NotificationService notificationService;
	BillsOfMonthService billsOfMonthService;

	@RequestMapping("${index}")
	public ModelAndView index() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated()) {
			ModelAndView modelAndView = new ModelAndView("home");
			if (userSettingsService.showAccountsSumOnHomepage()) {
				modelAndView.addObject("sumOfAllAccounts", accountService.getSumOfAll());
			}
			userService.addUsernameToModelAndView(modelAndView);
			
			notificationService.generateNotification();
			billsOfMonthService.payOutstandingAutomaticRepaymentBills();
			
			
			List<Account> accounts = accountService.getAll();
			modelAndView.addObject("accounts", accounts);
			
			long countOfActiveNotification = notificationService.getCountOfActiveNotification();
			modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
			return modelAndView;
		}
		return new ModelAndView("fail");
	}
	
}
