package pl.pfranczak.j2bills2.monolith.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.UserService;

@AllArgsConstructor
@Controller
public class IndexController {
	
	AccountService accountService;

	@RequestMapping("${index}")
	public ModelAndView index() {
		
		ModelAndView home = new ModelAndView("home");
		home.addObject("sumOfAllAccounts", accountService.getSumOfAll());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated())
			return home;
		return new ModelAndView("fail");
	}
	
}
