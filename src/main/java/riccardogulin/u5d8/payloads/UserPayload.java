package riccardogulin.u5d8.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserPayload {
	private String name;
	private String surname;
	private String email;
	private String password;
	private LocalDate dateOfBirth;
}
