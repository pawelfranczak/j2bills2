package pl.pfranczak.j2bills2.monolith.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;

@AllArgsConstructor
@Controller
public class UserSettingsController {
	
	private UserSettingsService userSettingsService; 
	private UserService userService;
	private AccountService accountService;
	
	@GetMapping("${userSettings}")	
	public ModelAndView showSettings() {
		ModelAndView modelAndView = new ModelAndView("settings");
		UserSettings userSettings = userSettingsService.get();
		Account billsAccount = userSettings.getBillsAccount();
		modelAndView.addObject("userSettings", userSettings);
		modelAndView.addObject("accounts", accountService.getAll());
		if (billsAccount != null) {
			modelAndView.addObject("accountID", billsAccount.getId());
		}
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@PostMapping("${userSettings}")	
	public ModelAndView modifySettings(@Valid UserSettings userSettings, BindingResult bindingResult) {
		userSettingsService.update(userSettings);
		return new ModelAndView("redirect:/settings");
	}

}
