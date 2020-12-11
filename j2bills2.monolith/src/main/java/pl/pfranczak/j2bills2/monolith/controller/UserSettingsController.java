package pl.pfranczak.j2bills2.monolith.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;

@AllArgsConstructor
@Controller
public class UserSettingsController {
	
	private UserSettingsService userSettingsService; 
	private UserService userService;
	
	@GetMapping("${userSettings}")	
	public ModelAndView showSettings() {
		ModelAndView modelAndView = new ModelAndView("settings");
		UserSettings userSettings = userSettingsService.get();
		modelAndView.addObject("userSettings", userSettings);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@PostMapping("${userSettings}")	
	public ModelAndView modifySettings(UserSettings userSettings) {
		userSettingsService.update(userSettings);
		return new ModelAndView("redirect:/settings");
	}

}
