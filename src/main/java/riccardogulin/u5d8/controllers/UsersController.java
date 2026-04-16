package riccardogulin.u5d8.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import riccardogulin.u5d8.entities.User;
import riccardogulin.u5d8.exceptions.ValidationException;
import riccardogulin.u5d8.payloads.NewUserRespDTO;
import riccardogulin.u5d8.payloads.UserDTO;
import riccardogulin.u5d8.payloads.UserPayload;
import riccardogulin.u5d8.services.UsersService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

	private final UsersService usersService;

	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	// 1. POST http://localhost:3001/users (+ req.body)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) // 201
	public NewUserRespDTO saveUser(@RequestBody @Validated UserDTO body, BindingResult validationResult) {


		if (validationResult.hasErrors()) {

			// String errors = validationResult.getFieldErrors().stream()
			// .map(error -> error.getDefaultMessage())
			// .collect(Collectors.joining(". "));
//			throw new ValidationException(errors);
			List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
			throw new ValidationException(errors);
		}

		User newUser = this.usersService.save(body);
		return new NewUserRespDTO(newUser.getUserId());
	}

	// 2. GET http://localhost:3001/users
	@GetMapping
	public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
	                           @RequestParam(defaultValue = "10") int size,
	                           @RequestParam(defaultValue = "surname") String sortBy) {
		return this.usersService.findAll(page, size, sortBy);
	}

	// 3. GET http://localhost:3001/users/{userId}
	@GetMapping("/{userId}")
	public User getById(@PathVariable UUID userId) {
		return this.usersService.findById(userId);
	}

	// 4. PUT http://localhost:3001/users/{userId} (+ req.body)
	@PutMapping("/{userId}")
	public User getByIdAndUpdate(@PathVariable UUID userId, @RequestBody UserPayload body) {
		return this.usersService.findByIdAndUpdate(userId, body);
	}

	// 5. DELETE http://localhost:3001/users/{userId}
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	public void getByIdAndDelete(@PathVariable UUID userId) {
		this.usersService.findByIdAndDelete(userId);
	}

}
