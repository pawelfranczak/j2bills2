package pl.pfranczak.j2bills2.monolith.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.repository.AccountRepository;
import pl.pfranczak.j2bills2.monolith.repository.JournalRepository;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;

@Service
public class AccountService extends CrudServiceImpl<Account, Long>{
	
	AccountRepository accountRepository;
	JournalRepository journalRepository;
	UserRepository userRepository;

	public AccountService(AccountRepository repository, JournalRepository journalRepository, UserRepository userRepository) {
		super.setRepository(repository);
		this.accountRepository = repository;
		this.journalRepository = journalRepository;
	}
	
	@Override
	public List<Account> getAll() {
		return accountRepository.findByOwner(getOwner());
	}
	
	@Override
	public Account get(Long id) {
		return accountRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(Account account) {
		account.setOwner(getOwner());
		account.setActive(true);
		super.create(account);
	}
	
	public void createWithJournalEntry(Account account, User user) {
		this.create(account);
		Journal journal = new Journal();
		journal.setAccount(account);
		journal.setBalanceOfAccountBeforeChange(BigDecimal.ZERO);
		journal.setDate(getTimestamp());
		journal.setDescription("Open Balance");
		journal.setOwner(getOwner());
		journal.setValue(account.getBalance());
		journal.setUser(user);
		journalRepository.save(journal);
	}
	
	public void create(Account account, UserAccount owner) {
		account.setOwner(owner);
		super.create(account);
	}
	
	
	public Account getTest(Long id) {
		Optional<Account> findById = accountRepository.findById(id);
		if (findById.isPresent())
			return findById.get();
		return null;
	}		
	
}
