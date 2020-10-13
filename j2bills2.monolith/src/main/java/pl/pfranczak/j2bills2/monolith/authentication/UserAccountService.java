package pl.pfranczak.j2bills2.monolith.authentication;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAccountService implements UserDetailsService {

	private final UserAccountRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ConfirmationTokenService confirmationTokenService;
	
	private final EmailService emailService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		final Optional<UserAccount> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			return optionalUser.get(); 
		}
		else {
			throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
		}
	}
	
	void signUpUser(UserAccount user) {
		final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		userRepository.save(user);
		final ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
	}
	
	void confirmUser(ConfirmationToken confirmationToken) {
		final UserAccount user = confirmationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);
		confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
	}

	void sendConfirmationMail(String userMail, String token) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setSubject("Mail Confirmation Link!");
		mailMessage.setFrom("<MAIL>");
		mailMessage.setText(
				"Thank you for registering. Please click on the below link to activate your account." + "http://localhost:8080/sign-up/confirm?token="
						+ token);

		emailService.sendEmail(mailMessage);
	}
	
	
}
