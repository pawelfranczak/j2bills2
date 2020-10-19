package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.service.AccountService;

@AllArgsConstructor
@Controller
@RequestMapping("${account}")
public class AccountController {
	
	private AccountService accountService; 
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("account/all");
		List<Account> accounts = accountService.getAll();
		modelAndView.addObject("accounts", accounts);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("account/one");
		Account account = accountService.get(id);
		modelAndView.addObject("account", account);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public String newEntity(Account account) {
		return "account/new";
	}

	@PostMapping("${new}")
	public String newEntityPost(Account account) {
		accountService.create(account);
		return "redirect:/account/all";
	}
	
	@GetMapping("${delete}")
	public ModelAndView deleteEntity() {
		ModelAndView modelAndView = new ModelAndView("account/delete");
		List<Account> accounts = accountService.getAll();
		modelAndView.addObject("accounts", accounts);
		return modelAndView;
	}

	@PostMapping("${delete}")
	public String deletePost(@RequestParam Long id) {
		accountService.deleteById(id);
		return "redirect:/account/delete";
	}

}
