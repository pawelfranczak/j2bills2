package pl.pfranczak.j2bills2.monolith.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.Account;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.User;
import pl.pfranczak.j2bills2.monolith.entity.UserSettings;
import pl.pfranczak.j2bills2.monolith.repository.UserSettingsRepository;

@Service
public class UserSettingsService extends CrudServiceImpl<UserSettings, Long>{

	UserSettingsRepository userSettingsRepository;
	
	public UserSettingsService(UserSettingsRepository userSettingsRepository) {
		super.setRepository(userSettingsRepository); 
		this.userSettingsRepository = userSettingsRepository;
	}
	
	public UserSettings get() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			return findByOwner.get(0);
		}
		return null;
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
	
	public User getDefaultUser() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getDefaultUser();
		}
		return null;
	}
	
	public Account getBillsAccount() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getBillsAccount();
		}
		return null;
	}
	
	public Account getBillsDifferenceAccount() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getBillsDifferenceAccount();
		}
		return null;
	}
	
	public int getGenerateNotificationBeforeDueDate1() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getGenerateNotificationBeforeDueDate1();
		}
		return 0;
	}
	
	public int getGenerateNotificationBeforeDueDate2() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getGenerateNotificationBeforeDueDate2();
		}
		return 0;
	}
	
	public int getGenerateNotificationBeforeDueDate3() {
		List<UserSettings> findByOwner = userSettingsRepository.findByOwner(getOwner());
		if (findByOwner != null && findByOwner.size() > 0) {
			UserSettings userSettings = findByOwner.get(0);
			return userSettings.getGenerateNotificationBeforeDueDate3();
		}
		return 0;
	}
	
	@Override
	public void update(UserSettings userSettings) {
		userSettings.setOwner(getOwner());
		super.update(userSettings);
	}

}
