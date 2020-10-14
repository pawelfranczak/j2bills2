package pl.pfranczak.j2bills2.monolith.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;

@Service
public class UserService extends CrudServiceImpl<User, Long>{

	UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		super.setRepository(userRepository);
		this.userRepository = userRepository;
	}
	
	@Override
	public List<User> getAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// to do magic here, @PreAuthorise? need to retrive only data which belongs to logged user by id
		UserAccount userAccount = (UserAccount) auth.getPrincipal();
		List<User> allForLoggedUser = getAllForLoggedUser(userAccount);
		return allForLoggedUser;
	}
	
	private List<User> getAllForLoggedUser(UserAccount owner) {
		return userRepository.findByOwner(owner);
	}
	
}
