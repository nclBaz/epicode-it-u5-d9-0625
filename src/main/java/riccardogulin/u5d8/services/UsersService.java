package riccardogulin.u5d8.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import riccardogulin.u5d8.entities.User;
import riccardogulin.u5d8.exceptions.BadRequestException;
import riccardogulin.u5d8.exceptions.NotFoundException;
import riccardogulin.u5d8.payloads.UserDTO;
import riccardogulin.u5d8.payloads.UserPayload;
import riccardogulin.u5d8.repositories.UsersRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UsersService {

	private final UsersRepository usersRepository;
	private final Cloudinary cloudinaryUploader;

	public UsersService(UsersRepository usersRepository, Cloudinary cloudinaryUploader) {
		this.usersRepository = usersRepository;
		this.cloudinaryUploader = cloudinaryUploader;
	}

	public User save(UserDTO body) {
		// 1. Verifichiamo che l'email ricevuta non sia già in uso
		if (this.usersRepository.existsByEmail(body.email()))
			throw new BadRequestException("L'indirizzo email " + body.email() + " è già in uso!");

		// 2. Salvo
		User newUser = new User(body.name(), body.surname(), body.email(), body.password(), body.dateOfBirth());
		User savedUser = this.usersRepository.save(newUser);

		// 3. Log
		log.info("L'utente con id " + savedUser.getUserId() + " è stato salvato correttamente!");

		// 4. Ritorno l'utente salvato
		return savedUser;
	}

	public Page<User> findAll(int page, int size, String sortBy) {
		if (size > 100 || size < 0) size = 10;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return this.usersRepository.findAll(pageable);
	}

	public User findById(UUID userId) {
		return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
	}

	public User findByIdAndUpdate(UUID userId, UserPayload body) {
		// 1. Cerchiamo l'utente tramite userId
		User found = this.findById(userId);

		// 2. Controllo che email non sia già in uso (lo faccio solo se sta effettivamente cambiando email)
		if (!found.getEmail().equals(body.getEmail())) {
			if (this.usersRepository.existsByEmail(body.getEmail()))
				throw new BadRequestException("L'indirizzo email " + body.getEmail() + " è già in uso!");
		}

		// 3. Modifico l'utente trovato
		found.setName(body.getName());
		found.setSurname(body.getSurname());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());
		found.setDateOfBirth(body.getDateOfBirth());
		found.setAvatarURL("https://ui-avatars.com/api?name=" + body.getName() + "+" + body.getSurname());

		// 4. Salvo
		User updateUser = this.usersRepository.save(found);

		// 5. Log
		log.info("L'utente " + updateUser.getUserId() + " è stato modificato correttamente");

		// 6. Ritorno l'utente modificato
		return updateUser;
	}

	public void findByIdAndDelete(UUID userId) {
		User found = this.findById(userId);
		this.usersRepository.delete(found);
	}

	public void avatarUpload(MultipartFile file, UUID userId) {
		// 1. Controlli (file non più grande di tot, tipologia di file deve essere solo .gif,...)
		// 2. find by id dell'utente

		// 3. Upload del file su Cloudinary
		try {
			Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			String url = (String) result.get("secure_url");
			System.out.println(url);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// 4. Cloudinary se l'upload va a buon fine ci tornerà l'URL dell'immagine caricata
		// setAvatarUrl("url_da_cloudinary")
		// save di user

		// 5. Return user con nuovo avatar

	}
}
