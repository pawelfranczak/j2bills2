package pl.pfranczak.j2bills2.monolith.service;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.repository.AccountRepository;

@Service
public class AccountService extends CrudServiceImpl<Account, Long>{

	public AccountService(AccountRepository repository) {
		super.setRepository(repository);
	}
	
}
