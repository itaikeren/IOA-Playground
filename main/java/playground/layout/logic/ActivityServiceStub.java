package playground.layout.logic;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import playground.exception.ActivityNotFoundException;

//@Service
public class ActivityServiceStub implements ActivityService{
	private Map<String, ActivityEntity> database;

	@PostConstruct
	public void init() {
		this.database = new HashMap<>();
	}
	
	@Override
	public void cleanup() {
		this.database.clear();
		
	}

	@Override
	public String performActivity(String playground, String email, ActivityEntity activity) throws ActivityNotFoundException {
		ActivityEntity rv = this.database.get(activity.getId());
		if (rv == null) {
			throw new ActivityNotFoundException("could not find activity by id: " + activity.getId());
		}
		System.err.println("Activity:"+rv.getType()+" was performed!");
		System.err.println("The user with email:"+email+" got 10 points!");
		return "Good";
	}

}
