package playground.layout.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import playground.exception.UserAlreadyExistsException;
import playground.exception.UserNotFoundException;
import playground.exception.inValidCodeException;

//@Service
public class UserServiesStub implements UserService {
	private Map<String, UserEntity> database;

	@PostConstruct
	public void init() {
		this.database = new HashMap<>();
	}

	@Override
	public UserEntity addNewUser(UserEntity userEntity) throws UserAlreadyExistsException {
		if (this.database.containsKey(userEntity.getEmail() + "#" + userEntity.getPlayground())) {
			throw new UserAlreadyExistsException("a user exists for: " + userEntity.getEmail());
		}
		this.database.put(userEntity.getEmail() + "#" + userEntity.getPlayground(), userEntity);
		return this.database.get(userEntity.getEmail() + "#" + userEntity.getPlayground());
	}

	@Override
	public UserEntity getUser(String playground, String email) throws UserNotFoundException {
		UserEntity rv = this.database.get(email+"#"+playground);
		if (rv == null) {
			throw new UserNotFoundException("could not find user by email: " + email);
		}
		return rv;
	}

	@Override
	public void cleanup() {
		this.database.clear();

	}

	@Override
	public List<UserEntity> getAllUsers(int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(String playground, String email, UserEntity update) {
		UserEntity existing = this.database.get(email+"#"+playground);
		boolean dirty = false;
		
		if (!update.getUsername().equals(existing.getUsername())) {
			existing.setUsername(update.getUsername());
			dirty = true;
		}
		
		if (!update.getAvatar().equals(existing.getAvatar())) {
			existing.setAvatar(update.getAvatar());
			dirty = true;
		}

		if (!update.getRole().equals(existing.getRole())) {
			existing.setRole(update.getRole());
			dirty = true;
		}
		
		if (dirty) {
			this.database.put(email+"#"+playground, existing);
		}

	}

	@Override
	public UserEntity checkCode(String playground, String email, int code) throws inValidCodeException {
		if (code != 2237) {
			throw new inValidCodeException("Your code is INVALID!");
		}
		return null;
	}

	@Override
	public UserEntity addNewUserWithOutSendEmail(UserEntity userEntity) throws UserAlreadyExistsException {
		return addNewUser(userEntity);
	}

}
