package playground.activities;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import playground.layout.logic.ActivityEntity;

@Component
public class readMessagesActivity implements PlaygroundActivity{
	
	@Override
	public Object invokeActivity(ActivityEntity activityEntity) {		
		return new LinkedHashMap<>();
	}
}
