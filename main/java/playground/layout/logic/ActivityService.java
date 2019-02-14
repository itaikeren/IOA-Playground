package playground.layout.logic;

import playground.exception.ActivityNotFoundException;

public interface ActivityService {

	public void cleanup();
	
	public String performActivity(String email, String userPlayground, ActivityEntity activity) throws ActivityNotFoundException;
}
