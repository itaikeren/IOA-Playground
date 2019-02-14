package playground.layout.logic;

import java.util.List;

import playground.exception.UserAlreadyExistsException;
import playground.exception.UserNotFoundException;
import playground.exception.inValidCodeException;

public interface UserService {
	public UserEntity addNewUser(UserEntity userEntity) throws UserAlreadyExistsException;

	public UserEntity getUser(String email, String playground) throws UserNotFoundException;
	
	public UserEntity checkCode(String email, String playground, int code) throws inValidCodeException;

	public void cleanup();

	public List<UserEntity> getAllUsers(int size, int page);

	public void updateUser(String email, String playground, UserEntity update) throws UserNotFoundException;

	UserEntity addNewUserWithOutSendEmail(UserEntity userEntity) throws UserAlreadyExistsException;

}
