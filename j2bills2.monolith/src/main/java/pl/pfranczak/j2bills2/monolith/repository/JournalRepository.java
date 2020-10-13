package pl.pfranczak.j2bills2.monolith.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.entity.Journal;

@Repository
public interface JournalRepository extends CrudRepository<Journal, Long>{
	
}
