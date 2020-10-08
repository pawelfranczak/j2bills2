package pl.pfranczak.j2bills2.monolith.authentication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "UserAccounts")
public class UserAccount {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String surname;

	private String email;
	
	private String password;
	
	@Builder.Default
	private UserAccountRole userRole = UserAccountRole.USER;
	
	@Builder.Default
	private Boolean locked = false;
	
	@Builder.Default
	private Boolean enabled = false;
}
