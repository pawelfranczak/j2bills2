package pl.pfranczak.j2bills2.monolith.service;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;

@Service
public class UserService extends CrudServiceImpl<User, Long>{

	public UserService(UserRepository userRepository) {
		super.setRepository(userRepository);
	}
	
}
