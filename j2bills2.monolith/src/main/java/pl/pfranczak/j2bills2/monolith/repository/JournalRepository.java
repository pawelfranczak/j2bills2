package pl.pfranczak.j2bills2.monolith.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;

@Repository
public interface JournalRepository extends CrudRepository<Journal, Long>{
	List<Journal> findByOwner(UserAccount owner);
	List<Journal> findByOwner(UserAccount owner, Pageable pageable);
	List<Journal> findByOwnerAndAccount(UserAccount owner, Account account);
	List<Journal> findByOwnerAndAccountOrderByIdDesc(UserAccount owner, Account account);
	Journal findByIdAndOwner(Long id, UserAccount owner);
	Long countByOwner(UserAccount owner);
	Journal findTopByOwnerOrderBySequenceDesc(UserAccount owner);
}
