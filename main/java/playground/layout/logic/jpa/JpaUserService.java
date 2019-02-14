package playground.layout.logic.jpa;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import playground.aop.ConfirmCheck;
import playground.aop.LoggerAspect;
import playground.aop.MyLogger;
import playground.aop.MyPerformanceCheck;
import playground.exception.UserAlreadyExistsException;
import playground.exception.UserNotFoundException;
import playground.exception.inValidCodeException;
import playground.jpadal.UserDao;
import playground.layout.logic.EmailService;
import playground.layout.logic.Mail;
import playground.layout.logic.UserEntity;
import playground.layout.logic.UserService;

@Service
public class JpaUserService implements UserService {
	
	@Value("${playground}")
	private String playground;
	
	private UserDao users;
	
    private EmailService emailService;
    
    private Log log = LogFactory.getLog(LoggerAspect.class);


	@Autowired
	public JpaUserService(UserDao users, EmailService emailService) {
		super();
		this.users = users;
		this.emailService = emailService;
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public UserEntity addNewUser(UserEntity userEntity) throws UserAlreadyExistsException {
		if (!this.users.existsById(userEntity.getIdentifier())) {
			userEntity.setCode((int) (Math.random() * (9999 - 1000 + 1)) + 1000);
			userEntity.setEmail(userEntity.getEmail().toLowerCase().trim());
			log.info("@@@@ "+ userEntity.getEmail() +" user code is:" + userEntity.getCode() + " @@@@");
			
			Mail mail = new Mail();
	        mail.setFrom("ioaplayground@outlook.com");
	        mail.setTo(userEntity.getEmail());
	        mail.setSubject("Your confirmation code for Playground 2019A.itaikeren");
	        mail.setContent( "Hello " + userEntity.getUsername()
            + ", thank you for register. Your confirmation code is: " + userEntity.getCode());
	        
	        emailService.sendSimpleMessage(mail);
			
			return this.users.save(userEntity);
		} else {
			throw new UserAlreadyExistsException("user already exisits in our playground with the email: " + userEntity.getEmail());
		}
	}
	
	@Override
	@MyLogger
	@MyPerformanceCheck
	// Used for tests //
	public UserEntity addNewUserWithOutSendEmail(UserEntity userEntity) throws UserAlreadyExistsException {
		if (!this.users.existsById(userEntity.getIdentifier())) {
			userEntity.setCode((int) (Math.random() * (9999 - 1000 + 1)) + 1000);
			userEntity.setEmail(userEntity.getEmail().toLowerCase().trim());
			log.info("@@@@ "+ userEntity.getEmail() +" user code is:" + userEntity.getCode() + " @@@@");
			
			return this.users.save(userEntity);
		} else {
			throw new UserAlreadyExistsException("user already exisits in our playground with the email: " + userEntity.getEmail());
		}
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	@ConfirmCheck
	public UserEntity getUser(String email, String playground) throws UserNotFoundException {
		return this.users.findById(email.toLowerCase().trim()+"#"+playground).orElseThrow(() -> new UserNotFoundException("No user with email: " + email + " in the playground:" + playground));
	}

	@Override
	@MyLogger
	public UserEntity checkCode(String email, String playground, int code) throws inValidCodeException {
		email = email.toLowerCase().trim();
		UserEntity ue = this.users.findById(email+"#"+playground).get();
		if (ue.getCode() == code) {
			ue.setConfirm(true);
			this.users.save(ue);
			return ue;
		} else {
			throw new inValidCodeException("Your code is INVALID!");
		}
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public void cleanup() {
		this.users.deleteAll();
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public List<UserEntity> getAllUsers(int size, int page) {
		return this.users.findAll(PageRequest.of(page, size, Direction.DESC, "id")).getContent();
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	@ConfirmCheck
	public void updateUser(String email, String playground, UserEntity update) throws UserNotFoundException {
		email = email.toLowerCase().trim();
		UserEntity existing = getUser(email, playground);

		if (!update.getUsername().equals(existing.getUsername())) {
			existing.setUsername(update.getUsername());
		}

		if (!update.getAvatar().equals(existing.getAvatar())) {
			existing.setAvatar(update.getAvatar());
		}

		if (!update.getRole().equals(existing.getRole())) {
			existing.setRole(update.getRole());
		}
		
		this.users.save(existing);

	}

/*	Not used in the course, saved for future development
 * @Override
	@MyLogger
	@AdminCheck
	public void deleteUser(String email, String playground, UserEntity deletedUser) throws UserNotFoundException {
		email = email.toLowerCase().trim();
		if (this.users.existsById(deletedUser.getEmail()+"#"+deletedUser.getPlayground())) {
			this.users.deleteById(deletedUser.getEmail()+"#"+deletedUser.getPlayground());
		} else {
			throw new UserNotFoundException("no user in our playground for email: " + email);
		}
	}*/

}
