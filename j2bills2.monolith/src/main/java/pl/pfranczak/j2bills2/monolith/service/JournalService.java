package pl.pfranczak.j2bills2.monolith.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.Movement;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.repository.JournalRepository;

@Service
public class JournalService extends CrudServiceImpl<Journal, Long>{

	private AccountService accountService;
	private JournalRepository journalRepository;
	
	public JournalService(JournalRepository repository, AccountService accountService) {
		super.setRepository(repository);
		this.accountService = accountService;
		this.journalRepository = repository;
	}
	
	public void create(Journal entity) {
		Account account = entity.getAccount();
		BigDecimal oldBalance = account.getBalance();
		account.setBalance(oldBalance.add(entity.getValue()));
		entity.setBalanceOfAccountBeforeChange(oldBalance);
		entity.setOwner(getOwner());
		entity.setDate(getTimestamp());
		repository.save(entity);
		accountService.update(account);
	}
	
	public void createMovement(Movement movement) {
		Journal journalSource = new Journal();
		journalSource.setAccount(movement.getSourceAccount());
		journalSource.setValue(movement.getValue().negate());
		journalSource.setDescription(movement.getDescription());
		journalSource.setUser(movement.getUser());
		create(journalSource);
		
		Journal journalTarget = new Journal();
		journalTarget.setAccount(movement.getTargetAccount());
		journalTarget.setValue(movement.getValue());
		journalTarget.setDescription(movement.getDescription());
		journalTarget.setUser(movement.getUser());
		create(journalTarget);
	}
	
	@Override
	public List<Journal> getAll() {
		return journalRepository.findByOwner(getOwner());
	}
	
	@Override
	public Journal get(Long id) {
		return journalRepository.findByIdAndOwner(id, getOwner());
	}	
	
	public List<Journal> getAllForAccount(Account account) {
		return journalRepository.findByOwnerAndAccount(getOwner(), account);
	}	
	
}
