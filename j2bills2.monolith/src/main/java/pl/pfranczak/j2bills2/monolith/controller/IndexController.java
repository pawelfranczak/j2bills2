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

@AllArgsConstructor
@Controller
public class IndexController {
	
	AccountService accountService;
	
	UserService userService;
	
	UserSettingsService userSettingsService;

	@RequestMapping("${index}")
	public ModelAndView index() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated()) {
			ModelAndView modelAndView = new ModelAndView("home");
			if (userSettingsService.showAccountsSumOnHomepage()) {
				modelAndView.addObject("sumOfAllAccounts", accountService.getSumOfAll());
			}
			userService.addUsernameToModelAndView(modelAndView);
			
			List<Account> accounts = accountService.getAll();
			modelAndView.addObject("accounts", accounts);
			
			return modelAndView;
		}
		return new ModelAndView("fail");
	}
	
}
