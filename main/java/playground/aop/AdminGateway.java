package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.jpadal.UserDao;
import playground.layout.UserTO;
import playground.layout.logic.UserEntity;

@Component
@Aspect
public class AdminGateway {
	private UserDao users;
	
	@Autowired
	public AdminGateway(UserDao users) {
		super();
		this.users = users;
	}
	
	@Around("@annotation(playground.aop.AdminCheck) && args(email,playground,..)")
	public Object logName (ProceedingJoinPoint pjp, String email, String playground) throws Throwable{
		UserEntity user = this.users.findById(email.toLowerCase().trim()+"#"+playground).get();
		if (!user.getRole().equalsIgnoreCase(UserTO.adminRole)) {
			throw new RuntimeException("Only admin can preform that action!");
		}
		
		return pjp.proceed();		
	}
}
