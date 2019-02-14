package playground.activities;

import org.springframework.stereotype.Component;

import playground.layout.logic.ActivityEntity;

@Component
public class spinActivity implements PlaygroundActivity{

	@Override
	public Object invokeActivity(ActivityEntity activityEntity) {
		int km = 0;
		try {
			String speed = activityEntity.getAttributes().get("speed").toString();
			if(speed.equalsIgnoreCase("fast")) {
				km = 10;
			} else if(speed.equalsIgnoreCase("normal")) {
				km = 5;
			} else if(speed.equalsIgnoreCase("stop")) {
				km = 0;
			}
			
			return km;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
