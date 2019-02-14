package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.jpadal.UserDao;
import playground.layout.logic.UserEntity;

@Component
@Aspect
public class ConfirmGateway {
	private UserDao users;
	
	@Autowired
	public ConfirmGateway(UserDao users) {
		super();
		this.users = users;
	}
	
	@Around("@annotation(playground.aop.ConfirmCheck) && args(email,playground,..)")
	public Object logName (ProceedingJoinPoint pjp, String email, String playground) throws Throwable{
		UserEntity user = this.users.findById(email.toLowerCase().trim()+"#"+playground).get();
		if (!user.isConfirm()) {
			throw new RuntimeException("Only confirmed users can preform that action!");
		}
		
		return pjp.proceed();		
	}
}
