package pl.pfranczak.j2bills2.monolith.controller;

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
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.Movement;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.JournalService;
import pl.pfranczak.j2bills2.monolith.service.UserService;

@AllArgsConstructor
@Controller
@RequestMapping("${journal}")
public class JournalController {

	private JournalService journalService;
	private UserService userService;
	private AccountService accountService;
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("journal/all");
		List<Journal> journalEntries = journalService.getAll();
		modelAndView.addObject("journalEntries", journalEntries);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("journal/one");
		Journal journalEntry = journalService.get(id);
		modelAndView.addObject("journalEntry", journalEntry);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Journal journalEntry) {
		return getDefaultModelAndViewForNewEntity();
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Journal journalEntry, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = getDefaultModelAndViewForNewEntity();
			if (journalEntry.getUser() != null) {
				modelAndView.addObject("userID", journalEntry.getUser().getId());
			}
			if (journalEntry.getAccount() != null) {
				modelAndView.addObject("accountID", journalEntry.getAccount().getId());
			}
			return modelAndView;
		}
		journalService.create(journalEntry);
		return new ModelAndView("redirect:/journal/new");
	}
	
	@GetMapping("${movement}")
	public ModelAndView newMovement(Movement movement) {
		ModelAndView modelAndView = new ModelAndView("journal/movement");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		List<Account> accounts = accountService.getAllActive();
		modelAndView.addObject("accounts", accounts);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${movement}")
	public ModelAndView newMovement(@Valid Movement movement, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("journal/movement");
			if (movement.getUser() != null) {
				modelAndView.addObject("userID", movement.getUser().getId());
			}
			if (movement.getSourceAccount() != null) {
				modelAndView.addObject("sourceAccountID", movement.getSourceAccount().getId());
			}
			if (movement.getTargetAccount() != null) {
				modelAndView.addObject("targetAccountID", movement.getTargetAccount().getId());
			}
			userService.addUsernameToModelAndView(modelAndView);
			return modelAndView;
		}
		journalService.createMovement(movement);
		return new ModelAndView("redirect:/journal/movement");
	}

	private ModelAndView getDefaultModelAndViewForNewEntity() {
		ModelAndView modelAndView = new ModelAndView("journal/new");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		List<Account> accounts = accountService.getAllActive();
		modelAndView.addObject("accounts", accounts);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

}
