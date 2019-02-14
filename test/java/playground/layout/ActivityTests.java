package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import playground.layout.logic.ActivityService;
import playground.layout.logic.ElementService;
import playground.layout.logic.Location;
import playground.layout.logic.UserEntity;
import playground.layout.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ActivityTests {
	
	@Value("${playground}")
	private String playground;
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private UserService userService;
	@Autowired
	private ElementService elementService;
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	private String url;
	
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		
		url = "http://localhost:" + port + "/playground/activities";
		System.err.println(this.url);
		
	}
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		// cleanup database
		this.activityService.cleanup();
		this.userService.cleanup();
		this.elementService.cleanup();
	}
	
	@Test
	public void testServerIsUp () throws Exception {
		
	}
	
	//////////////////////////////////////////////////////////
	
	@Test
	public void testPreformActivity() throws Exception{
		Map<String, Object> activityAttributes = new HashMap<>();
		activityAttributes.put("color", "blue");
		
		Location location = new Location(0,0);
		String name = "Carrousel";
		Date expirationDate = null;
		String type = "Carrousel";
		Map<String, Object> elementAttributes = new HashMap<>();
		elementAttributes.put("speed", 0);
		elementAttributes.put("color", "red");
		String email = "itai@gmail.com";
		
		UserTO memberUser = new UserTO("omer@gmail.com","omero","Member","www.google.com/img/superman.jpg");
		memberUser.setPlayground(playground);
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");
		adminUser.setPlayground(playground);
		ElementTO element = new ElementTO(location, name, expirationDate, type, elementAttributes, playground, email);
		
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		UserEntity ue2 = this.userService.addNewUserWithOutSendEmail(memberUser.toEntity());
		this.userService.checkCode(ue2.getEmail(), ue2.getPlayground(), ue2.getCode());
		
		this.elementService.addNewElement(adminUser.getEmail(), playground, element.toEntity());
		
		// Given server is up and the database contains "Carrousel" element and user "omer@gmail.com" and user "itai@gmail.com"
		ActivityTO activity = new ActivityTO("2019A.itaikeren","2019A.itaikeren","1","changeColor","2019A.itaikeren","omer@gmail.com",activityAttributes);
		
		// When I POST /playground/activities/2019A.itaikeren/omer@gmail.com
		//	with headers 
		//	Accept:application/json 
		//	Content-Type: application/json
		//	And with body {"elementPlayground":"2019A.itaikeren","elementId":"1","type":"changeColor","attributes":{"speed":0, "color":"red"}}
		String preformActivity = this.restTemplate.postForObject(
				this.url + "/{userPlayground}/{email}", 
				activity,
				String.class, 
				"2019A.itaikeren",
				"omer@gmail.com");

		// Then the response status is 200 
		assertThat(preformActivity)
			.isNotNull();
	}
}
