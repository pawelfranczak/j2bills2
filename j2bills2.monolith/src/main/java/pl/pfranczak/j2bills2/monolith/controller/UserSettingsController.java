package pl.pfranczak.j2bills2.monolith.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
public class UserSettingsController {
	
	private UserSettingsService userSettingsService; 
	private UserService userService;
	private AccountService accountService;
	private NotificationService notificationService;
	
	@GetMapping("${userSettings}")	
	public ModelAndView showSettings() {
		ModelAndView modelAndView = new ModelAndView("settings");
		UserSettings userSettings = userSettingsService.get();
		Account billsAccount = userSettings.getBillsAccount();
		Account billsDifferenceAccount = userSettings.getBillsDifferenceAccount();
		User defaultUser = userSettings.getDefaultUser();
		modelAndView.addObject("userSettings", userSettings);
		modelAndView.addObject("accounts", accountService.getAll());
		modelAndView.addObject("users", userService.getAll());
		if (billsAccount != null) {
			modelAndView.addObject("accountID", billsAccount.getId());
		}
		if (billsDifferenceAccount != null) {
			modelAndView.addObject("accountDifferenceID", billsDifferenceAccount.getId());
		}
		if (defaultUser != null) {
			modelAndView.addObject("defaultUserID", defaultUser.getId());
		}
		
		userService.addUsernameToModelAndView(modelAndView);
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@PostMapping("${userSettings}")	
	public ModelAndView modifySettings(@Valid UserSettings userSettings, BindingResult bindingResult) {
		userSettingsService.update(userSettings);
		return new ModelAndView("redirect:/settings");
	}

}
