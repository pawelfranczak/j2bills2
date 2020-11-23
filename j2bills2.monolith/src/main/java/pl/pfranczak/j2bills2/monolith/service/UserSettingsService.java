package pl.pfranczak.j2bills2.monolith.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.repository.UserSettingsRepository;

@Service
public class UserSettingsService extends CrudServiceImpl<UserSettings, Long>{

	UserSettingsRepository userSettingsRepository;
	
	public UserSettingsService(UserSettingsRepository userSettingsRepository) {
		super.setRepository(userSettingsRepository); 
		this.userSettingsRepository = userSettingsRepository;
	}
	
	public boolean showAccountsSumOnHomepage() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.isShowAccountsSumOnHomepage();
		}
		return false;
	}
	
	public Long getHowManyJournalEntriesOnJournalPage() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getHowManyJournalEntriesOnJournalPage();
		}
		return Long.MAX_VALUE;
	}

}
