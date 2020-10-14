package pl.pfranczak.j2bills2.monolith.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class CrudServiceImpl<T, ID> implements CrudService<T, ID> {

	CrudRepository<T, ID> repository;
	
	public void setRepository(CrudRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	@Override
	public void create(T entity) {
		repository.save(entity);
	}

	@Override
	public T get(ID id) {
		Optional<T> find = repository.findById(id);
		if (find.isPresent()) {
			return find.get();
		}
		return null;
	}
	
	@Override
	public void update(T entity) {
		repository.save(entity);
	}

	@Override
	public void delete(T entity) {
		repository.delete(entity);
	}

	@Override
	public void deleteById(ID id) {
		repository.deleteById(id);
	}
	
	@Override
	public List<T> getAll() {
		Iterable<T> findAll = repository.findAll();
		List<T> all = new ArrayList<T>();
		findAll.forEach(all::add);
		return all;
	}

}
