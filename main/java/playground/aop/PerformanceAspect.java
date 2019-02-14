package playground.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceAspect {
	private Log log = LogFactory.getLog(LoggerAspect.class);
	
	@Around("@annotation(playground.aop.MyPerformanceCheck)")
	public Object checkPerformance (ProceedingJoinPoint pjp) throws Throwable{
		if(log.isTraceEnabled()){
			long startTime = System.nanoTime();
			
			try {
				return pjp.proceed();
			}finally {
				long endTime = System.nanoTime();
				log.trace((endTime - startTime) + "ns");
			}
		}else {
			return pjp.proceed();
		}
	}
	
	
}
