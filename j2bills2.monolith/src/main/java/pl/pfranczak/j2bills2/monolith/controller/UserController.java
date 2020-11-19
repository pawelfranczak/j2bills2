package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.service.UserService;

@AllArgsConstructor
@Controller
@RequestMapping("${user}")
public class UserController {
	
	private UserService userService; 
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("user/all");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("user/one");
		User user = userService.get(id);
		modelAndView.addObject("user", user);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public String newEntity(User user) {
		// TODO userService.addUsernameToModelAndView(modelAndView);
		return "user/new";
	}

	@PostMapping("${new}")
	public String newEntityPost(@Valid User user, BindingResult bindingResult) {
		// TODO userService.addUsernameToModelAndView(modelAndView);
		if (bindingResult.hasErrors()) {
			return "user/new";
		}
		userService.create(user);
		return "redirect:/user/all";
	}
	
	@GetMapping("${delete}")
	public ModelAndView deleteEntity() {
		ModelAndView modelAndView = new ModelAndView("user/delete");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${delete}")
	public String deletePost(@RequestParam Long id) {
		userService.deleteById(id);
		return "redirect:/user/delete";
	}

}
