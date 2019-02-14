package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

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

import playground.layout.logic.UserEntity;
import playground.layout.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserTests {
	
	@Value("${playground}")
	private String playground;
	
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
		
		url = "http://localhost:" + port + "/playground/users";
		System.err.println(this.url);
		
		this.jackson = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		// cleanup database
		this.userService.cleanup();
	}
	
	@Test
	public void testServerIsUp () throws Exception {
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testSuccessfulUserCreation () throws Exception{
		String email = "itai@gmail.com";
		String username = "itaik";
		String role = UserTO.adminRole;
		String avatar = "www.google.com/img/superman.jpg";
		
		//Given server is up
		
		//When I POST /messages 
		//	with headers 
		//	Accept:application/json 
		//	Content-Type: application/json
		//	And with body {"email":"itai@gmail.com", "username":"itaik", "role":"Admin", "avatar":"www.google.com/img/superman.jpg"}
		UserTO newUser = new UserTO(email,username,role,avatar);
		UserEntity actualValue = this.userService.addNewUserWithOutSendEmail(newUser.toEntity());
		
		//Then the response status is 200 
		//And the database contains for email: "itai@gmail.com" 
		// the object {"playground":"2019A.itaikeren", "email":"itai@gmail.com", "username":"itaik", "role":"Admin", "points":100L, "avatar":"www.google.com/img/superman.jpg"}
		
		assertThat(actualValue)
			.extracting("playground", "email", "username", "role", "points", "avatar")
			.containsExactly(playground, email, username, role, 100L, avatar);
	}
	
	@Test
	public void testValidAConfirmationCode() throws Exception{
		int code;
		String email = "itai@gmail.com";
		String username = "itaik";
		String role = UserTO.adminRole;
		String avatar = "www.google.com/img/superman.jpg";
		
		// Given server is up and user perform Register
		UserEntity ue = this.userService.addNewUserWithOutSendEmail(new UserEntity(playground, email, username, role, avatar));
		code = ue.getCode();
		// When I GET /playground/users/confirm/2019A.itaikeren/itai@gmail.com/XXXX
		// with headers Accept:application/json
		this.userService.checkCode(email, playground, code);
		
		UserTO actualUser = new UserTO(this.userService.getUser(email, playground));
		
		// then the response status is 200
		assertThat(actualUser)
			.isNotNull()
			.extracting("playground", "email", "username", "role", "points", "avatar")
			.containsExactly(playground, email, username, role, 100L, avatar);
	}
	
	@Test(expected=Exception.class)
	public void testInvalidConfirmationCode() throws Exception{
		int code = 1234;
		String email = "itai@gmail.com";
//		String playground = "2019A.itaikeren";
		String username = "itaik";
		String role = UserTO.adminRole;
		String avatar = "www.google.com/img/superman.jpg";
		
		// Given server is up and user perform Register
		this.userService.addNewUser(new UserEntity(playground, email, username, role, avatar));
		
		// When I GET /playground/users/confirm/2019A.itaikeren/itai@gmail.com/1234
		// with headers Accept:application/json
		this.userService.checkCode(email, playground, code);
		
		UserTO actualUser = new UserTO(this.userService.getUser(email, playground));
		
		// then response status is <> 200
		assertThat(actualUser)
			.isNotNull()
			.extracting("playground", "email", "userName", "role", "points", "avatar")
			.containsExactly(playground, email, username, role, 100L, avatar);
	}
	
	@Test
	public void testLoginToTheSystemWithUserEmail() throws Exception{
		String email = "itai@gmail.com";
//		String playground = "2019A.itaikeren";
		String username = "itaik";
		String role = UserTO.adminRole;
		String avatar = "www.google.com/img/superman.jpg";
		
		// Given server is up and the database contains {"email":"itai@gmail.com"}
		// and the user is already confirm
		UserEntity ue = this.userService.addNewUserWithOutSendEmail(new UserEntity(playground, email, username, role, avatar));
		
		//		int code = this.userService.addNewUser(new UserEntity(playground, email, username, role, avatar)).getCode();
		this.userService.checkCode(email, playground, ue.getCode());
		
		// When I GET /playground/users/login/2019A.itaikeren/itai@gmail.com
		// with headers Accept:application/json
		UserTO actualUser = this.restTemplate.getForObject(
				this.url + "/login/{playground}/{email}", 
				UserTO.class, 
				playground,
				email);
		
		// then the response status is 200
		assertThat(actualUser)
			.isNotNull();
	}
	
	@Test(expected=Exception.class)
	public void testLoginToTheSystemWithInvalidEmail() throws Exception{
		// When I GET /playground/users/login/2019A.itaikeren/itai@gmail.com
		// with headers Accept:application/json
		this.restTemplate.getForObject(
				this.url + "/login/{playground}/{email}", 
				UserTO.class, 
				"2019A.itaikeren",
				"itai@gmail.com");
	}
	
	@Test
	public void testUpdateUser() throws Exception{
		String email = "itai@gmail.com";
//		String playground = "2019A.itaikeren";
		int code;
		
		//Given server is up 
		//And the database contains {"playground":"2019A.itaikeren","email":"itai@gmail.com", "username":"itaik", "avatar":"www.google.com/img/superman.jpg","role":"Admin", "points":100}
		code = this.userService.addNewUserWithOutSendEmail(
				this.jackson.readValue("{\"email\":\"itai@gmail.com\", \"playground\":\"2019A.itaikeren\", \"username\":\"itaik\", \"avatar\":\"www.google.com/img/superman.jpg\",\"role\":\"Admin\", \"points\":100, \"identifier\":\"itai@gmail.com#2019A.itaikeren\"}", UserEntity.class)).getCode();
		this.userService.checkCode(email, playground, code);
		// When I PUT /playground/users/2019A.itaikeren/itai@gmail.com
		// with headers
		// Content-Type: application/json
		// And with body {"playground":"2019A.itaikeren","email":"itai@gmail.com", "username":"omero", "avatar":"www.google.com/img/superman.jpg","role":"Admin", "points":100}
		
		String updatedUserJson = "{\"email\":\"itai@gmail.com\", \"playground\":\"2019A.itaikeren\", \"username\":\"omero\", \"avatar\":\"www.google.com/img/superman.jpg\",\"role\":\"Admin\", \"points\":100}";
		UserTO updatedUser = this.jackson.readValue(updatedUserJson, UserTO.class);
		
		this.restTemplate.put(
				this.url + "/{playground}/{email}", 
				updatedUser, 
				playground,
				email);
		
		// Then the response status is 200 
		//And the database contains for email: "itai@gmail.com" the object {"playground":"2019A.itaikeren","email":"itai@gmail.com", "username":"omero", "role":"Admin", "avatar":"www.google.com/img/superman.jpg"}
		
		UserEntity actualUser = this.userService.getUser(email, playground);
		String actualUserJson = this.jackson.writeValueAsString(actualUser);
		
		
		UserEntity expectedUser = this.jackson.readValue("{\"email\":\"itai@gmail.com\", \"playground\":\"2019A.itaikeren\", \"username\":\"omero\", \"avatar\":\"www.google.com/img/superman.jpg\",\"role\":\"Admin\", \"points\":100,  \"code\":"+actualUser.getCode()+", \"confirm\":true, \"identifier\":\"itai@gmail.com#2019A.itaikeren\"}", UserEntity.class);
		String expectedUserJson = this.jackson.writeValueAsString(expectedUser);
		assertThat(actualUserJson)
			.isEqualTo(expectedUserJson);
	}
	
}
