package pl.pfranczak.j2bills2.monolith.controller.bills;

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
import pl.pfranczak.j2bills2.monolith.entity.bills.Bill;
import pl.pfranczak.j2bills2.monolith.service.UserService;
import pl.pfranczak.j2bills2.monolith.service.bills.BillService;

@AllArgsConstructor
@Controller
@RequestMapping("${bill}")
public class BillController {
	
	private BillService billService; 
	private UserService userService; 
	
	@GetMapping("${all}")	
	public ModelAndView showAll() {
		ModelAndView modelAndView = new ModelAndView("bill/all");
		List<Bill> bills = billService.getAll();
		modelAndView.addObject("bills", bills);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("/{id}")	
	public ModelAndView showOne(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("bill/one");
		Bill bill = billService.get(id);
		modelAndView.addObject("bill", bill);
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}
	
	@GetMapping("${new}")
	public ModelAndView newEntity(Bill bill) {
		ModelAndView modelAndView = new ModelAndView("bill/new");
		userService.addUsernameToModelAndView(modelAndView);
		return modelAndView;
	}

	@PostMapping("${new}")
	public ModelAndView newEntityPost(@Valid Bill bill, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("bill/new");
			userService.addUsernameToModelAndView(modelAndView);
			return modelAndView;
		}
		billService.create(bill);
		return new ModelAndView("redirect:/bill/all");
	}

	
//	@GetMapping("${modify}/{id}")
//	public ModelAndView modifywEntity(@PathVariable("id") Long id) {
//		ModelAndView modelAndView = new ModelAndView("account/modify");
//		Account account = accountService.get(id);
//		modelAndView.addObject("account", account);
//		List<User> users = userService.getAll();
//		modelAndView.addObject("users", users);
//		userService.addUsernameToModelAndView(modelAndView);
//		return modelAndView;
//	}
//
//	// TODO Add validation
//	@PostMapping("${modify}")
//	public String modifyEntityPost(Account account) {
//		accountService.update(account);
//		return "redirect:/account/all";
//	}

}
