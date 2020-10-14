package pl.pfranczak.j2bills2.monolith.authentication;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserAccountController {

	private final UserAccountService userService;

	private final ConfirmationTokenService confirmationTokenService;

	@GetMapping("${login}")
	String signIn() {
		return "login";
	}

	@GetMapping("${register}")
	String signUpPage(UserAccount user) {
		return "register";
	}

	@PostMapping("${register}")
	String signUp(UserAccount user) {
		userService.signUpUser(user);
		return "redirect:/login";
	}

	@GetMapping("${confirm}")
	String confirmMail(@RequestParam("token") String token) {
		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
		optionalConfirmationToken.ifPresent(userService::confirmUser);
		return "redirect:/login";
	}
	
}
