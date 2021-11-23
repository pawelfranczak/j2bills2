package pl.pfranczak.j2bills2.monolith.controller.notifications;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.notification.Notification;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
@RequestMapping("${notification}")
public class NotificationController {
	
	private NotificationService notificationService;

	@GetMapping("${all}")
	public ModelAndView showAllGet() {
		ModelAndView modelAndView = new ModelAndView("notification/all");
		notificationService.generateNotification();
		List<Notification> notifications = notificationService.getAll();
		modelAndView.addObject("notifications", notifications);
		return modelAndView;
	}
	
	@GetMapping("${history}")
	public ModelAndView showAllHistoricalGet() {
		ModelAndView modelAndView = new ModelAndView("notification/history");
		notificationService.generateNotification();
		List<Notification> notifications = notificationService.getAllNotActive();
		modelAndView.addObject("notifications", notifications);
		return modelAndView;
	}
	
}
