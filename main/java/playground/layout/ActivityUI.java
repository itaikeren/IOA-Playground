package playground.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.exception.ActivityNotFoundException;
import playground.layout.logic.ActivityService;

@RestController
public class ActivityUI {

	private ActivityService activities;
	
	@Autowired
	public void setActivities(ActivityService activities) {
		this.activities = activities;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/playground/activities/{userPlayground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object userActivity(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email, @RequestBody ActivityTO activity) throws ActivityNotFoundException {
		return this.activities.performActivity(email, userPlayground, activity.toEntity());
	}

}