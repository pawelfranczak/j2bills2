package pl.pfranczak.j2bills2.monolith.service;

import org.springframework.stereotype.Service;

@Service
public interface CrudService<T, ID> {
	public boolean create(T entity);
	public T get(ID id);
	public boolean update(T entity);
	public boolean delete(T entity);
	public boolean deleteById(ID id);
}
