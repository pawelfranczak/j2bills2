package pl.pfranczak.j2bills2.monolith.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.repository.JournalRepository;

@Service
public class JournalService extends CrudServiceImpl<Journal, Long>{

	private AccountService accountService;
	
	public JournalService(JournalRepository repository, AccountService accountService) {
		super.setRepository(repository);
		this.accountService = accountService;
	}
	
	@Override
	public void create(Journal entity) {
		Account account = entity.getAccount();
		BigDecimal oldBalance = account.getBalance();
		account.setBalance(oldBalance.add(entity.getValue()));
		entity.setBalanceOfAccountBeforeChange(oldBalance);
		repository.save(entity);
		accountService.update(account);
	}

	
}
