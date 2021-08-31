package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Alerts;
import pl.pfranczak.j2bills2.monolith.entity.AlertsGenerationByDay;
import pl.pfranczak.j2bills2.monolith.service.AlertsGenerationByDayService;
import pl.pfranczak.j2bills2.monolith.service.AlertsService;
import pl.pfranczak.j2bills2.monolith.service.UserService;

@AllArgsConstructor
@Controller
public class AlertsController {
	
	AlertsService alertsService;
	UserService userService;

	@RequestMapping("${alerts}")
	public ModelAndView alerts() {
		alertsService.generateAlerts();
		ModelAndView modelAndView = new ModelAndView("alerts");
		List<Alerts> alerts = alertsService.getAll();
		modelAndView.addObject("alerts", alerts);
		return modelAndView;
	}
	
}
