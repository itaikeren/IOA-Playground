package playground.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.exception.UserNotConfirmException;
import playground.exception.UserNotFoundException;
import playground.layout.logic.UserService;

@RestController
public class UserUI {

	@Value("${playground}")
	private String playground;
	
	private UserService users;

	@Autowired
	public void setUsers(UserService users) {
		this.users = users;
	}

	@RequestMapping(method = RequestMethod.POST, path = "playground/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserTO createUser(@RequestBody NewUserForm newUser){
		UserTO ut = new UserTO(newUser.getEmail(),newUser.getUsername(), newUser.getRole(),newUser.getAvatar());
		ut.setPlayground(playground);
		return new UserTO(this.users.addNewUser(ut.toEntity()));
	}


	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/confirm/{playground}/{email}/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO confirmUser(@PathVariable("playground") String playground, @PathVariable("email") String email,
			@PathVariable("code") int code) throws UserNotFoundException {
		return new UserTO(users.checkCode(email, playground, code));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/login/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String playground, @PathVariable("email") String email)
			throws UserNotFoundException {
		if (users.getUser(email, playground).isConfirm()) {
			return new UserTO(users.getUser(email, playground));
		} else {
			throw new UserNotConfirmException("That user is not confirm");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/playground/users/{playground}/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable("email") String email, @PathVariable("playground") String playground,
			@RequestBody UserTO updatedUser) throws Exception {
		this.users.updateUser(email, playground, updatedUser.toEntity());
	}

/*	Not used in the course, saved for future development
 * @RequestMapping(method = RequestMethod.GET, path = "/playground/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<UserTO> getUsers(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		ArrayList<UserTO> allUsers = new ArrayList<>();
		ArrayList<UserEntity> rv = new ArrayList<>(users.getAllUsers(size, page));
		for (UserEntity user : rv) {
			allUsers.add(new UserTO(user));
		}

		return allUsers;
	}*/

	
/*	Not used in the course, saved for future development
 * @RequestMapping(method = RequestMethod.POST, path = "/playground/users/delete/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable("email") String email, @PathVariable("playground") String playground,
			@RequestBody UserTO deletedUser) throws UserNotFoundException {
		this.users.deleteUser(email, playground, deletedUser.toEntity());
	}*/
}