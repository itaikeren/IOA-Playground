package playground.layout.logic.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.activities.Message;
import playground.activities.PlaygroundActivity;
import playground.aop.ConfirmCheck;
import playground.aop.MemberCheck;
import playground.aop.MyLogger;
import playground.aop.MyPerformanceCheck;
import playground.exception.ActivityNotFoundException;
import playground.jpadal.ActivityDao;
import playground.jpadal.ElementDao;
import playground.jpadal.GeneratedNumber;
import playground.jpadal.UserDao;
import playground.layout.logic.ActivityEntity;
import playground.layout.logic.ActivityService;
import playground.layout.logic.ElementEntity;
import playground.layout.logic.UserEntity;

@Service
public class JpaActivityService implements ActivityService {

	@Value("${playground}")
	private String playground;

	private UserDao users;
	private ElementDao elements;
	private ActivityDao activities;

	private ConfigurableApplicationContext spring;
	private ObjectMapper jackson;

	@Autowired
	public JpaActivityService(UserDao users, ElementDao elements, ActivityDao activities,
			ConfigurableApplicationContext spring, ObjectMapper jackson) {
		super();
		this.users = users;
		this.elements = elements;
		this.activities = activities;
		this.spring = spring;
		this.jackson = jackson;

		if (activities.count() > 0) {
			List<ActivityEntity> allActivities = this.activities
					.findAll(PageRequest.of(0, 5, Direction.DESC, "creationDate")).getContent();
			GeneratedNumber.setActivityId(Long.parseLong(allActivities.get(0).getId()));
		}
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public void cleanup() {
		this.users.deleteAll();
		this.elements.deleteAll();
		this.activities.deleteAll();
		GeneratedNumber.resetId();
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	@ConfirmCheck
	@MemberCheck
	public String performActivity(String email, String userPlayground, ActivityEntity activitiy)
			throws ActivityNotFoundException {

		// Save the activity in the database
		long number = GeneratedNumber.getNextActivityValue();
		activitiy.setId("" + number);
		activitiy.setPlayground(playground);
		activitiy.setPlayerPlayground(userPlayground);
		activitiy.setPlayerEmail(email);
		this.activities.save(activitiy);

		String result = "";
		try {
			String elementIdentifier = activitiy.getElementId() + "#" + activitiy.getElementPlayground();
			String type = activitiy.getType();
			String targetClassName = "playground.activities." + type + "Activity";
			Class<?> pluginClass = Class.forName(targetClassName);
			// autowire activity
			PlaygroundActivity activity = (PlaygroundActivity) this.spring.getBean(pluginClass);

			Object rv = activity.invokeActivity(activitiy);

			if (type.equals("postMessage")) {
				if (!this.elements.findById(elementIdentifier).get().getType().equalsIgnoreCase("Message Board")) {
					throw new RuntimeException("You can perform that activity only on Message Board type element!");
				}
				Message msg = (Message) rv;
				ElementEntity ee = elements.findById(elementIdentifier).get();
				ee.getAttributes().putAll(msg.getMessage());
				elements.save(ee);
				result = updatePoints(type, rv, email, userPlayground);
				result = this.jackson.writeValueAsString(result);

			} else if (type.equals("readMessages")) {
				if (!this.elements.findById(elementIdentifier).get().getType().equalsIgnoreCase("Message Board")) {
					throw new RuntimeException("You can perform that activity only on Message Board type element!");
				}
				Map<String, Object> allMessages = (Map<String, Object>) rv;
				allMessages.putAll(elements.findById(elementIdentifier).get().getAttributes());
				if (!allMessages.isEmpty()) {
					result = this.jackson.writeValueAsString(allMessages);
				} else {
					result = this.jackson.writeValueAsString("No messages. Go ahead and post the first message!");
				}

			} else if (type.equals("spin")) {
				if (!this.elements.findById(elementIdentifier).get().getType().equalsIgnoreCase("Carrousel")) {
					throw new RuntimeException("You can perform that activity only on Carrousel type element!");
				}
				int km = (int) rv;
				ElementEntity ee = elements.findById(elementIdentifier).get();
				if(km != 0) {
					int currentSpeed = (int) ee.getAttributes().get("speed");
					if (currentSpeed + km > 20) {
						result = "It's getting too fast. Maybe we shuold stop the carrousel?";
						result = this.jackson.writeValueAsString(result);
					} else {
						ee.getAttributes().put("speed", currentSpeed + km);
						this.elements.save(ee);
						result = updatePoints(type, rv, email, userPlayground);
						result = this.jackson.writeValueAsString(result);
					}
					
				} else {
					ee.getAttributes().put("speed", 0);
					this.elements.save(ee);
					result = updatePoints(type, rv, email, userPlayground);
					result = this.jackson.writeValueAsString(result);
				}

			} else if (type.equals("changeColor")) {
				if (!this.elements.findById(elementIdentifier).get().getType().equalsIgnoreCase("Carrousel")) {
					throw new RuntimeException("You can perform that activity only on Carrousel type element!");
				}
				String color = (String) rv;
				ElementEntity ee = elements.findById(elementIdentifier).get();
				ee.getAttributes().put("color", color);
				this.elements.save(ee);
				result = updatePoints(type, rv, email, userPlayground);
				result = this.jackson.writeValueAsString(result);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@MyLogger
	@MyPerformanceCheck
	public String updatePoints(String type, Object rv, String userEmail, String userPlayground) {
		int points = 0;

		if (type.equals("postMessage")) {
			Message msg = (Message) rv;

			Map.Entry<String, String> entry = msg.getMessage().entrySet().iterator().next();
			String key = entry.getKey();
			String time = key.split(":")[1];

			if (time.endsWith("5") || time.endsWith("0")) {
				points = 15;
			} else {
				points = 10;
			}

		} else if (type.equals("spin")) {
			points = 5;

		} else if (type.equals("changeColor")) {
			points = 12;
		}

		UserEntity ue = this.users.findById(userEmail + "#" + userPlayground).get();
		ue.addPoints(points);
		this.users.save(ue);
		return "You got " + points + " points for that activity!";
	}

}
