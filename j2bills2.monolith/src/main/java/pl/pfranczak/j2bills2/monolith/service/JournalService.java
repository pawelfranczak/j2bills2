package pl.pfranczak.j2bills2.monolith.service;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.repository.JournalRepository;

@Service
public class JournalService extends CrudServiceImpl<Journal, Long>{

	public JournalService(JournalRepository repository) {
		super.setRepository(repository);
	}
	
}
