package pl.pfranczak.j2bills2.monolith.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "owner_id")
	private UserAccount owner;
	
	boolean showAccountsSumOnHomepage;
	
	Long howManyJournalEntriesOnJournalPage;
	
}
