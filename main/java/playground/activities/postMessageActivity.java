package playground.activities;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.layout.logic.ActivityEntity;

@Component
public class postMessageActivity implements PlaygroundActivity{
	
	@Override
	public Object invokeActivity(ActivityEntity activityEntity) {		
		try {
			String msg = activityEntity.getAttributes().get("message").toString();
			return new Message(msg);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
