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
public class AlertsService extends CrudServiceImpl<Alerts, Long>{
	
	AlertsRepository alertsRepository;
	AlertsGenerationByDayRepository alertsGenerationByDayRepository;
	UserRepository userRepository;

	public AlertsService(AlertsRepository repository, AlertsGenerationByDayRepository alertsGenerationByDayRepository, UserRepository userRepository) {
		super.setRepository(repository);
		this.alertsRepository = repository;
		this.alertsGenerationByDayRepository = alertsGenerationByDayRepository;
	}
	
	@Override
	public List<Alerts> getAll() {
		return alertsRepository.findByOwner(getOwner());
	}
	
	@Override
	public Alerts get(Long id) {
		return alertsRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(Alerts alerts) {
		alerts.setOwner(getOwner());
		super.create(alerts);
	}
	
	@Override
	public void update(Alerts alerts) {
		alerts.setOwner(getOwner());
		super.update(alerts);
	}
	
	public void generateAlerts() {
		AlertsGenerationByDay generatedForDay = alertsGenerationByDayRepository.findByOwnerAndDay(getOwner(), LocalDate.now());
		if (generatedForDay == null) {
			// TODO: generate alerts
		}
	}

}
