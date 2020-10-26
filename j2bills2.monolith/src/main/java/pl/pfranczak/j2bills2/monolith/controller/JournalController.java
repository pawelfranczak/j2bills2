package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("journal/one");
		Journal journalEntry = journalService.get(id);
		modelAndView.addObject("journalEntry", journalEntry);
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

	private ModelAndView getDefaultModelAndViewForNewEntity() {
		ModelAndView modelAndView = new ModelAndView("journal/new");
		List<User> users = userService.getAll();
		modelAndView.addObject("users", users);
		List<Account> accounts = accountService.getAll();
		modelAndView.addObject("accounts", accounts);
		return modelAndView;
	}

}
