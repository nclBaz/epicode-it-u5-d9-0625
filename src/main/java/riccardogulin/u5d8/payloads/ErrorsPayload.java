package riccardogulin.u5d8.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorsPayload {
	private String message;
	private LocalDateTime timestamp;
}
