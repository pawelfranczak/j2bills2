package pl.pfranczak.j2bills2.monolith.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.service.AccountService;

@AllArgsConstructor
@Controller
public class IndexController {
	
	AccountService accountService;

	@RequestMapping("${index}")
	public ModelAndView index() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated()) {
			ModelAndView home = new ModelAndView("home");
			home.addObject("sumOfAllAccounts", accountService.getSumOfAll());
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			home.addObject("userName", userDetails.getUsername());
			return home;
		}
		return new ModelAndView("fail");
	}
	
}
