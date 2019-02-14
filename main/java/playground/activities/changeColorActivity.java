package playground.activities;

import org.springframework.stereotype.Component;

import playground.layout.logic.ActivityEntity;

@Component
public class changeColorActivity implements PlaygroundActivity{

	@Override
	public Object invokeActivity(ActivityEntity activityEntity) {
		return activityEntity.getAttributes().get("color").toString();
	}

}
