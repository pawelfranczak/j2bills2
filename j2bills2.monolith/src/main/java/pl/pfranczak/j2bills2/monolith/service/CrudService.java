package pl.pfranczak.j2bills2.monolith.service;

import org.springframework.stereotype.Service;

@Service
public interface CrudService<T, ID> {
	public void create(T entity);
	public T get(ID id);
	public void update(T entity);
	public void delete(T entity);
	public void deleteById(ID id);
}
