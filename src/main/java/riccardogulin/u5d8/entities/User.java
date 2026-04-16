package riccardogulin.u5d8.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private UUID userId;

	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String surname;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;
	@Column(name = "avatar_url")
	private String avatarURL;

	public User(String name, String surname, String email, String password, LocalDate dateOfBirth) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.avatarURL = "https://ui-avatars.com/api?name=" + name + "+" + surname;
	}
}
