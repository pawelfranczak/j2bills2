package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
		return modelAndView;
	}

}
