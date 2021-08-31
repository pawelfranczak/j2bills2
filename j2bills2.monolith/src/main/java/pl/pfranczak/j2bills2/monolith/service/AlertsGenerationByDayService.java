package pl.pfranczak.j2bills2.monolith.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Alerts;
import pl.pfranczak.j2bills2.monolith.entity.AlertsGenerationByDay;
import pl.pfranczak.j2bills2.monolith.repository.AlertsGenerationByDayRepository;
import pl.pfranczak.j2bills2.monolith.repository.AlertsRepository;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;

@Service
public class AlertsGenerationByDayService extends CrudServiceImpl<AlertsGenerationByDay, Long>{
	
	AlertsGenerationByDayRepository alertsRepository;
	UserRepository userRepository;

	public AlertsGenerationByDayService(AlertsGenerationByDayRepository repository, UserRepository userRepository) {
		super.setRepository(repository);
		this.alertsRepository = repository;
	}
	
	@Override
	public List<AlertsGenerationByDay> getAll() {
		return alertsRepository.findByOwner(getOwner());
	}
	
	@Override
	public AlertsGenerationByDay get(Long id) {
		return alertsRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(AlertsGenerationByDay alerts) {
		alerts.setOwner(getOwner());
		super.create(alerts);
	}
	
	@Override
	public void update(AlertsGenerationByDay alerts) {
		alerts.setOwner(getOwner());
		super.update(alerts);
	}
	
	public boolean alertsGeneratedForDay(LocalDate day) {
		AlertsGenerationByDay alertsGenerationByDay = alertsRepository.findByOwnerAndDay(getOwner(), day);
		if (alertsGenerationByDay != null) {
			return true;
		}
		return false;
	}

}
