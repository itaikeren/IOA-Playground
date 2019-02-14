package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.layout.logic.ElementEntity;
import playground.layout.logic.ElementService;
import playground.layout.logic.Location;
import playground.layout.logic.UserEntity;
import playground.layout.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ElementTests {

	@Value("${playground}")
	private String playground;
	
	@Autowired
	private ElementService elementService;
	
	@Autowired
	private UserService userService;
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	private String url;
	
	private ObjectMapper jackson;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		
		url = "http://localhost:" + port + "/playground/elements";
		System.err.println(this.url);
		
		this.jackson = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		// cleanup database
		this.elementService.cleanup();
		this.userService.cleanup();
		
	}
	
	@Test
	public void testServerIsUp () throws Exception {
		
	}
	
	//////////////////////////////////////////////////////////
	
	@Test
	public void testSuccessfulElementCreation () throws Exception{
		Location location = new Location(0,0);
		String name = "Carrousel";
//		String playground = "2019A.itaikeren";
		Date expirationDate = null;
		String type = "Carrousel Toy";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		String email = "itai@gmail.com";
		
		//Given server is up and the database contain use "itai@gmail.com"
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		//When I POST /playground/elements/2019A.itaikeren/itai@gmail.com 
		//	with headers 
		//	Accept:application/json 
		//	Content-Type: application/json
		//	And with body {"location":{"x":0,"y":0}, "name":"carrousel", "expirationDate":"null", "type":"toy", "attributes":{"color":"red","speed":0}}
		ElementTO newElement = new ElementTO(location, name, expirationDate, type, attributes, playground, email);
				
		ElementTO et = this.restTemplate.postForObject(
				this.url + "/"+playground+"/itai@gmail.com", 
				newElement, 
				ElementTO.class);
		
		//Then the response status is 200 
		//And the database contains for id: "1" 
		// the object {"playground":"2019A.itaikeren","id":"1","location":{"x":0,"y":0}, "name":"carrousel", "creationDate":SOME DATE, "expirationDate":"null", "type":"default", "attributes":{"color":"red","speed":"medium"}, "creatorPlayground":"2019A.itaikeren", "creatorEmail":"default@email.com"}
		
		ElementEntity actualValue = this.elementService.getElement(ue1.getPlayground(), playground, email, et.getId());
		
		assertThat(actualValue)
			.extracting("playground", "id", "x", "y", "expirationDate", "type", "attributes", "creatorPlayground", "creatorEmail")
			.containsExactly(playground, et.getId(), location.getX(), location.getY(), expirationDate, type, attributes, playground, email);
	}
	
	@Test
	public void testUpdateElement() throws Exception{
		
		//Given server is up 
		//And the database contains {"playground":"2019A.itaikeren","id":"1", "x":0, "y":0, "name":"Carrousel", "creationDate":SOME DATE, "expirationDate":null, "type":"toy", "attributes":{"color":"red","speed":0}, "creatorPlayground":"2019A.itaikeren", "creatorEmail":"default@email.com"}
		//And user "itai@gmail.com"
		
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		this.elementService.addNewElement("itai@gmail.com", playground, 
				this.jackson.readValue("{\"x\":0, \"y\":0, \"name\":\"Carrousel\",\"creationDate\":null, \"expirationDate\":null, \"type\":\"Carrousel Toy\", \"attributes\":{\"color\":\"red\",\"speed\":0}, \"creatorPlayground\":\"2019A.itaikeren\", \"creatorEmail\":\"itai@gmail.com\"}", ElementEntity.class));
		
		// When I PUT /playground/elements/2019A.itaikeren/itai@gmail.com/2019A.itaikeren/1
		// with headers
		// Content-Type: application/json
		// And with body {"playground":"2019A.itaikeren","id":"1", "location":{"x":0, "y":0}, "name":"Fast Carrousel", "creationDate":SOME DATE, "expirationDate":null, "type":"toy", "attributes":{"color":"red","speed":20}, "creatorPlayground":"2019A.itaikeren", "creatorEmail":"itai@gmail.com"}
		
		String updatedElementJson = "{\"location\":{\"x\":0, \"y\":0}, \"name\":\"Fast Carrousel\", \"expirationDate\":null, \"type\":\"Carrousel Toy\", \"attributes\":{\"color\":\"red\",\"speed\":20}, \"creatorPlayground\":\"2019A.itaikeren\", \"creatorEmail\":\"itai@gmail.com\"}";
		ElementTO updatedElement = this.jackson.readValue(updatedElementJson, ElementTO.class);
		
		this.restTemplate.put(
				this.url + "/{userPlayground}/{email}/{playground}/{id}", 
				updatedElement, 
				playground,
				"itai@gmail.com",
				playground,
				"1");
		
		// Then the response status is 200 
		//And the database contains for id: "1" the object {"playground":"2019A.itaikeren","id":"1", "x":0, "y":0, "name":"Fast Carrousel", "creationDate":SOME DATE, "expirationDate":null, "type":"toy", "attributes":{"color":"red","speed":20}, "creatorPlayground":"2019A.itaikeren", "creatorEmail":"itai@gmail.com"}
		
		ElementEntity actualElement = this.elementService.getElement(ue1.getPlayground(), playground,"itai@gmail.com", "1");
		String actualElementJson = this.jackson.writeValueAsString(actualElement);
		
		
		ElementEntity expectedElement = this.jackson.readValue("{\"playground\":\"2019A.itaikeren\", \"id\":\"1\", \"x\":0, \"y\":0, \"name\":\"Fast Carrousel\", \"expirationDate\":null, \"type\":\"Carrousel Toy\", \"attributes\":{\"color\":\"red\",\"speed\":20}, \"creatorPlayground\":\"2019A.itaikeren\", \"creatorEmail\":\"itai@gmail.com\", \"identifier\":\"1#2019A.itaikeren\"}", ElementEntity.class);
		expectedElement.setCreationDate(actualElement.getCreationDate());
		String expectedElementJson = this.jackson.writeValueAsString(expectedElement);
		assertThat(actualElementJson)
			.isEqualTo(expectedElementJson);
	}
	
	@Test
	public void testGetElement() throws Exception{
		String id = "1";
		double x = 0;
		double y = 0;
		String name = "Carrousel";
		Date expirationDate = null;
		String type = "toy";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		
		// Given server is up and the database contains {"id":"1"} and user "itai@gmail.com"
		
		UserTO adminUser = new UserTO("itai@gmail.com","itaik",UserTO.adminRole,"www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
				
		this.elementService.addNewElement("itai@gmail.com", playground, new ElementEntity(x, y, name, expirationDate, type, attributes));
		
		// When I GET /playground/elements/2019A.itaikeren/itai@gmail.com/2019A.itaikeren/1
		// with headers Accept:application/json
		ElementTO actualElement = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/{playground}/{id}", 
				ElementTO.class, 
				playground,
				"itai@gmail.com",
				playground,
				"1");

		// Then the response status is 200 
		//And the database contains for id: "1" the object {"playground":"2019A.itaikeren","id":"1", "x":0, "y":0, "name":"Carrousel", "creationDate":SOME DATE, "expirationDate":null, "type":"toy", "attributes":{"color":"red","speed":0}, "creatorPlayground":"2019A.itaikeren", "creatorEmail":"itai@gmail.com"}
		assertThat(actualElement)
			.isNotNull()
			.extracting("id")
			.containsExactly(id);
	}
	
	@Test
	public void testGetAllElementsSuccessfullyForAdmin () throws Exception{
		String email = "itai@gmail.com";
		double x = 0;
		double y = 0;
		String name1 = "Carrousel1";
		String name2 = "Carrousel2";
		String name3 = "Carrousel3";
		Date expirationDate = new Date();
		String type = "Carrousel Toy";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		
		// Given server is up and the database contains {"id":"1", "id":"2", "id":"3"} and user "itai@gmail.com"
		
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name1, expirationDate, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name2, expirationDate, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name3, expirationDate, type, attributes));
		
		// When I GET /playground/elements/2019A.itaikeren/itai@gmail.com/all
		// with headers Accept:application/json
		ArrayList<ElementTO> actualElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/all",
				ArrayList.class,
				playground,
				email);
		
		// Then the response status is 200 
		//And size is 3
		assertThat(actualElements)
			.isNotNull()
			.hasSize(3);
	}
	
	@Test
	public void testGetAllElementsSuccessfullyForMember () throws Exception{
		String email = "itai@gmail.com";
		double x = 0;
		double y = 0;
		String name1 = "Carrousel1";
		String name2 = "Carrousel2";
		String name3 = "Carrousel3";
		Date expirationDate1 = new Date();
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, 1);
		Date expirationDate2 = c.getTime();
		String type = "toy";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		
		// Given server is up and the database contains {"id":"1", "id":"2", "id":"3"} and user "itai@gmail.com" and user "omer@gmail.com"
		
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		UserTO memberUser = new UserTO("omer@gmail.com","omer","Member","www.google.com/img/superman.jpg");		
		memberUser.setPlayground(playground);
		UserEntity ue2 = this.userService.addNewUserWithOutSendEmail(memberUser.toEntity());
		this.userService.checkCode(ue2.getEmail(), ue2.getPlayground(), ue2.getCode());
		
		
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name1, expirationDate1, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name2, expirationDate1, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(x, y, name3, expirationDate2, type, attributes));
		
		// When I GET /playground/elements/2019A.itaikeren/itai@gmail.com/all
		// with headers Accept:application/json
		ArrayList<ElementTO> actualElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/all",
				ArrayList.class,
				playground,
				"omer@gmail.com");
		
		// Then the response status is 200 
		//And size is 1
		assertThat(actualElements)
			.isNotNull()
			.hasSize(1);
	}
	
	@Test
	public void testGetAllElementsNearXYSuccessfully () throws Exception{
		String email = "itai@gmail.com";
		String name = "Carrousel";
		Date expirationDate = null;
		String type = "toy";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		// Given server is up and the database contains {"id":"1", "id":"2", "id":"3"} and user "itai@gmail.com"
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		this.elementService.addNewElement(email, playground, new ElementEntity(0, 5, name, expirationDate, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(20, 0, name, expirationDate, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(4, 5, name, expirationDate, type, attributes));
		
		// When I GET /playground/elements/2019A.itaikeren/itai@gmail.com/near/0/0/0
		// with headers Accept:application/json
		ArrayList<ElementTO> actualElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}?page={page}&size={size}",
				ArrayList.class,
				playground,
				email,
				0,
				0,
				10,
				0,
				10);
		
		// Then the response status is 200 
		//And size is 2
		assertThat(actualElements)
			.isNotNull()
			.hasSize(2);
	}
	
	@Test
	public void testGetAllElementsThatColorRedAttributeSuccessfully () throws Exception{
		String email = "itai@gmail.com";
		String name = "Carrousel";
		Date expirationDate = null;
		String type = "default";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("speed", 0);
		
		Map<String, Object> attributes2 = new HashMap<>();
		attributes2.put("color", "blue");
		// Given server is up and the database contains {"id":"1", "id":"2", "id":"3"} and user "itai@gmail.com"
		
		UserTO adminUser = new UserTO("itai@gmail.com","itaik","Admin","www.google.com/img/superman.jpg");		
		adminUser.setPlayground(playground);
		UserEntity ue1 = this.userService.addNewUserWithOutSendEmail(adminUser.toEntity());
		this.userService.checkCode(ue1.getEmail(), ue1.getPlayground(), ue1.getCode());
		
		this.elementService.addNewElement(email, playground, new ElementEntity(0, 0, name, expirationDate, type, attributes));
		this.elementService.addNewElement(email, playground, new ElementEntity(0, 0, name, expirationDate, type, attributes2));
		this.elementService.addNewElement(email, playground, new ElementEntity(4, 5, name, expirationDate, type, attributes));
		
		// When I GET /playground/elements/2019A.itaikeren/itai@gmail.com/search/color/red
		// with headers Accept:application/json
		ArrayList<ElementTO> actualElements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}",
				ArrayList.class,
				playground,
				email,
				"color",
				"red");
		
		// Then the response status is 200 
		//And size is 2
		assertThat(actualElements)
			.isNotNull()
			.hasSize(2);
	}
}
