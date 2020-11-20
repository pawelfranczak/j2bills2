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
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.JournalService;
import pl.pfranczak.j2bills2.monolith.service.UserService;

@AllArgsConstructor
@Controller
@RequestMapping("${account}")
public class AccountController {
	
	private AccountService accountService; 
	private UserService userService;
	private JournalService journalService;
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("account/all");
		List<Account> accounts = accountService.getAll();
		modelAndView.addObject("accounts", accounts);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("account/one");
		Account account = accountService.get(id);
		modelAndView.addObject("account", account);
		List<Journal> journalEntries = journalService.getAllForAccount(account);
		modelAndView.addObject("journalEntries", journalEntries);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Account account) {
		ModelAndView modelAndView = new ModelAndView("account/new");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Account account, BindingResult bindingResult, User user) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("account/new");
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
		accountService.createWithJournalEntry(account, user);
		return new ModelAndView("redirect:/account/all");
	}
	
	@GetMapping("${modify}/{id}")
	public ModelAndView modifywEntity(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("account/modify");
		Account account = accountService.get(id);
		modelAndView.addObject("account", account);
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	// TODO Add validation
	@PostMapping("${modify}")
	public String modifyEntityPost(Account account) {
		accountService.update(account);
		return "redirect:/account/all";
	}
	
	@GetMapping("${delete}")
	public ModelAndView deleteEntity() {
		ModelAndView modelAndView = new ModelAndView("account/delete");
		List<Account> accounts = accountService.getAll();
		modelAndView.addObject("accounts", accounts);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${delete}")
	public String deletePost(@RequestParam Long id) {
		accountService.deleteById(id);
		return "redirect:/account/delete";
	}

}
