package pl.pfranczak.j2bills2.monolith.service;

import java.util.List;
import java.util.Optional;

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
		return userRepository.findByOwner(getOwner());
	}
	
	@Override
	public User get(Long id) {
		return userRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(User user) {
		user.setOwner(getOwner());
		super.create(user);
	}
	
	public void create(User user, UserAccount owner) {
		user.setOwner(owner);
		super.create(user);
	}
	
	
	public User getTest(Long id) {
		Optional<User> findById = userRepository.findById(id);
		if (findById.isPresent())
			return findById.get();
		return null;
	}	
	
}
