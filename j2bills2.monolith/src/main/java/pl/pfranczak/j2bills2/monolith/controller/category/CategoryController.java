package pl.pfranczak.j2bills2.monolith.controller.category;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.category.CategoryService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
@RequestMapping("${category}")
public class CategoryController {
	
	private CategoryService categoryService; 
	private UserService userService;
	private NotificationService notificationService;

	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("category/all");
		List<Category> categories = categoryService.getAll();
		modelAndView.addObject("categories", categories);
		userService.addUsernameToModelAndView(modelAndView);
		
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Category category) {
		ModelAndView modelAndView = new ModelAndView("category/new");
		userService.addUsernameToModelAndView(modelAndView);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Category category, BindingResult bindingResult, User user) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("category/new");
			List<User> users = userService.getAll();
			modelAndView.addObject("users", users);
			if (user.getId() != null) {
				modelAndView.addObject("userID", user.getId());
			} else {
				modelAndView.addObject("userError", "must not be blank");
			}
			userService.addUsernameToModelAndView(modelAndView);
			return modelAndView;
		}
		categoryService.create(category);
		return new ModelAndView("redirect:/category/new");
	}
	
	@GetMapping("${change_active_state}/{id}")	
	public ModelAndView changeActiveState(@PathVariable("id") Long id) {
		Category category = categoryService.get(id);
		category.setActive(!category.isActive());
		categoryService.update(category);
		return new ModelAndView("redirect:/category/all");
	}
}
