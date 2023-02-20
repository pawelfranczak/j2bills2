package pl.pfranczak.j2bills2.monolith.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.service.AccountService;
import pl.pfranczak.j2bills2.monolith.service.JournalService;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.category.CategoryService;
import pl.pfranczak.j2bills2.monolith.service.category.SubCategoryService;

@AllArgsConstructor
@Controller
@RequestMapping("${journal}")
public class JournalController {

	private JournalService journalService;
	private UserService userService;
	private AccountService accountService;
	private UserSettingsService userSettingsService;
	private CategoryService	categoryService;
	private SubCategoryService subCategoryService;
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		return new ModelAndView("redirect:/journal/all/0");
	}
	
	@GetMapping("${all}/{page}")	
	public ModelAndView showAll(@PathVariable("page") Long page) {
		ModelAndView modelAndView = new ModelAndView("journal/all");
		
		Long entriesOnPage = userSettingsService.getHowManyJournalEntriesOnJournalPage();
		Pageable pageable = PageRequest.of(page.intValue(), entriesOnPage.intValue(), Sort.by("id").descending());
		
		List<Journal> journalEntries = journalService.getAll(pageable);
		modelAndView.addObject("journalEntries", journalEntries);
		
		Long countOfEntries = journalService.getCountOfJournalEntries();
		modelAndView.addObject("countOfEntries", countOfEntries);
		modelAndView.addObject("entriesOnPage", entriesOnPage);
		modelAndView.addObject("countOfPages", calculateCountOfPages(entriesOnPage, countOfEntries));
		calculateSequenceInformation(modelAndView, page, entriesOnPage, countOfEntries);
		userService.addUsernameToModelAndView(modelAndView);
		
		return modelAndView;
	}
	
	private void calculateSequenceInformation(ModelAndView modelAndView, Long page, Long entriesOnPage, Long countOfEntries) {
		// sequenceOfFirstEntry
		// sequenceOfLastEntry
		
		Long firstSequenceOnPage = Long.MIN_VALUE;
		Long lastSequenceOnPage = Long.MAX_VALUE;
		
		if (entriesOnPage.compareTo(countOfEntries) >= 0) {
			firstSequenceOnPage = countOfEntries;
			lastSequenceOnPage = 1L;
		} else {
			firstSequenceOnPage = countOfEntries - (page * entriesOnPage);
			lastSequenceOnPage = firstSequenceOnPage - entriesOnPage + 1L;
			if (lastSequenceOnPage.compareTo(1L) < 0) {
				lastSequenceOnPage = 1L;
			}
		}
		modelAndView.addObject("firstSequenceOnPage", firstSequenceOnPage);
		modelAndView.addObject("lastSequenceOnPage", lastSequenceOnPage);
	}
	
	private Long calculateCountOfPages(Long entriesOnPage, Long countOfEntries) {
		return (long)(Math.ceil((double)countOfEntries / entriesOnPage));
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
		User defaultUser = userSettingsService.getDefaultUser();
		if (defaultUser != null) {
			modelAndView.addObject("userID", defaultUser.getId());
		}
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
			modelAndView.addObject("defaultUserID", userSettingsService.getDefaultUser());
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
		User defaultUser = userSettingsService.getDefaultUser();
		if (defaultUser != null) {
			modelAndView.addObject("userID", defaultUser.getId());
		}
		List<Category> categories = categoryService.getAllActive();
		modelAndView.addObject("categories", categories);
		List<SubCategory> subCategories = subCategoryService.getAllWithActiveCategory();
		modelAndView.addObject("subCategories", subCategories);
		return modelAndView;
	}

}
