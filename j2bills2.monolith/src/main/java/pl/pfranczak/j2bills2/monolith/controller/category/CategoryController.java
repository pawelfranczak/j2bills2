package pl.pfranczak.j2bills2.monolith.controller.category;

import java.util.Arrays;
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
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.UserSettingsService;
import pl.pfranczak.j2bills2.monolith.service.category.CategoryService;
import pl.pfranczak.j2bills2.monolith.service.category.SubCategoryService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
@RequestMapping("${category}")
public class CategoryController {
	
	private CategoryService categoryService;
	private SubCategoryService subCategoryService;
	private UserService userService;
	private NotificationService notificationService;
	private UserSettingsService userSettingsService;

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
	
	@GetMapping("${install}")
	public ModelAndView installCategories() {
		
		if (userSettingsService.get().isCategoryInstaled()) {
			return new ModelAndView("redirect:/sub_category/all");
		} else {
			UserSettings userSettings = userSettingsService.get();
			userSettings.setCategoryInstaled(true);
			userSettingsService.update(userSettings);
		}

		String category = "Dzieci";
		List<String> subCategories = Arrays.asList("Zajęcia dodatkowe", "Kieszonkowe", "Opieka zdrowotna",
				"Opieka nad dziećmi", "Odzież", "Szkoła", "Zabawki", "Inne");
		Category categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		Category existingCategory = categoryService.getByName(category);

		storeSubCategory(existingCategory, subCategories);

		category = "Zobowiązania";
		subCategories = Arrays.asList("Karty kredytowe", "Pożyczki studenckie", "Inne pożyczki", "Podatki",
				"Opłaty lokalne", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Edukacja";
		subCategories = Arrays.asList("Czesne", "Książki", "Lekcje muzyki", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Rozrywka";
		subCategories = Arrays.asList("Książki", "Koncerty", "Mecze", "Hobby", "Filmy", "Muzyka",
				"Aktywność na świeżym powietrzu", "Fotografia", "Sport", "Teatr", "Telewizja", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Potrzeby codzienne";
		subCategories = Arrays.asList("Artykuły spożywcze", "Restauracje", "Kosmetyki", "Odzież", "Pranie", "Fryzjer",
				"Subskrypcje", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Podarunki";
		subCategories = Arrays.asList("Podarunki", "Datki (organizacje charytatywne)", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Dom";
		subCategories = Arrays.asList("Czynsz", "Kredyt hipoteczny", "Podatki od nieruchomości", "Utrzymanie czystości",
				"Ogród/trawnik", "Środki czystości", "Konserwacja", "Modernizacje", "Przeprowadzka", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Zdrowie";
		subCategories = Arrays.asList("Lekarze pierwszego kontaktu", "Lekarze specjaliści", "Apteka", "Nagłe wypadki",
				"Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Ubezpieczenie";
		subCategories = Arrays.asList("Samochód", "Zdrowie", "Dom", "Życie", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Zwierzęta";
		subCategories = Arrays.asList("Karma", "Weterynarz", "Zabawki", "Środki czystości", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Technologia";
		subCategories = Arrays.asList("Domeny i hosting", "Usługi internetowe", "Sprzęt", "Oprogramowanie", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Transport";
		subCategories = Arrays.asList("Paliwo", "Opłaty komunikacyjne", "Naprawy", "Rejestracja",
				"Materiały eksploatacyjne", "Transport publiczny", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Podróże";
		subCategories = Arrays.asList("Loty", "Hotele", "Jedzenie", "Transport", "Rozrywka", "Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Media";
		subCategories = Arrays.asList("Telefon", "Telewizja", "Internet", "Prąd", "Ogrzewanie", "Woda", "Śmieci",
				"Inne");
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		category = "Inne";
		subCategories = Arrays.asList(

		);
		categoryToSave = new Category();
		categoryToSave.setName(category);
		categoryService.create(categoryToSave);
		existingCategory = categoryService.getByName(category);
		storeSubCategory(existingCategory, subCategories);

		return new ModelAndView("redirect:/sub_category/all");
	}
	
	private void storeSubCategory(Category category, List<String> subCategories) {
		for (String sub : subCategories) {
			SubCategory subCategory = new SubCategory();
			subCategory.setCategory(category);
			subCategory.setName(sub);
			subCategoryService.create(subCategory);
		}
	}
	
}
