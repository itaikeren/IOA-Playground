package playground.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;

public class ActiveElementWindow {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/activities";

	private Stage stage;
	private Scene scene;
	private Label element = new Label("Element");
	private ComboBox<String> elementValue = new ComboBox<>();
	private Label activity = new Label("Activity");
	private ComboBox<String> activityValue = new ComboBox<>();
	private Label message = new Label("Your Message");
	private TextArea messageInput = new TextArea();
	private Label speed = new Label("Speed");
	private ComboBox<String> speedInput = new ComboBox<>();
	private Label color = new Label("Color");
	private ComboBox<String> colorInput = new ComboBox<>();
	private Button actionBt = new Button();
	private ArrayList<ElementTO> allElements = new ArrayList<>();
	private ElementTO currentElement = new ElementTO();
	private Map<String, Object> attributes = new HashMap<>();

	public ActiveElementWindow(UserTO user) {
		stage = new Stage();
		stage.setTitle("Active Element");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		ObjectMapper mapper = new ObjectMapper();

		JsonNode elements = this.restTemplate.getForObject(
				ClientApplication.url+"/elements/{userPlayground}/{email}/all", JsonNode.class,
				user.getPlayground(), user.getEmail());

		try {
			allElements = mapper.readValue(mapper.treeAsTokens(elements), new TypeReference<ArrayList<ElementTO>>() {
			});
		} catch (JsonParseException ex) {
			new AlertBox("Error", ex.getMessage());
		} catch (JsonMappingException ex) {
			new AlertBox("Error", ex.getMessage());
		} catch (IOException ex) {
			new AlertBox("Error", ex.getMessage());
		}

		// Design inputs
		for (ElementTO e : allElements) {
			if (user.getRole().equals(UserTO.adminRole)) {
				elementValue.getItems().add(e.getName());
			} else {
				if(e.getExpirationDate() != null) {
					if (!e.getExpirationDate().before(new Date())) {
						elementValue.getItems().add(e.getName());
					}
				} else {
					elementValue.getItems().add(e.getName());
				}
				
			}
		}

		message.setVisible(false);
		messageInput.setVisible(false);
		speed.setVisible(false);
		speedInput.setVisible(false);
		color.setVisible(false);
		colorInput.setVisible(false);
		actionBt.setVisible(false);

		messageInput.setMaxSize(250, 80);
		colorInput.getItems().addAll("Orange", "Red", "Blue", "Green");
		speedInput.getItems().addAll("Fast", "Normal", "Stop");

		elementValue.setOnAction(e -> {
			ElementTO selectedElement = new ElementTO();
			for (ElementTO et : allElements) {
				if (et.getName().equals(elementValue.getValue())) {
					selectedElement = et;
				}
			}

			currentElement = this.restTemplate.getForObject(
					ClientApplication.url+"/elements/{userPlayground}/{email}/{playground}/{id}",
					ElementTO.class, user.getPlayground(), user.getEmail(), selectedElement.getPlayground(),
					selectedElement.getId());

			if (currentElement.getType().equals("Message Board")) {
				activityValue.getItems().clear();
				activityValue.getItems().addAll("Post new message", "Read all messages");

			} else {
				activityValue.getItems().clear();
				activityValue.getItems().addAll("Spin", "Change Color");
			}

			message.setVisible(false);
			messageInput.setVisible(false);
			speed.setVisible(false);
			speedInput.setVisible(false);
			color.setVisible(false);
			colorInput.setVisible(false);
			actionBt.setVisible(false);

			stage.setHeight(200);

		});

		activityValue.setOnAction(e -> {
			if (!activityValue.getItems().isEmpty()) {
				if (activityValue.getValue().equals("Post new message")) {
					message.setVisible(true);
					messageInput.setVisible(true);
					speed.setVisible(false);
					speedInput.setVisible(false);
					color.setVisible(false);
					colorInput.setVisible(false);
					actionBt.setVisible(true);

					actionBt.setText("Post message");
					
					actionBt.setOnAction(a -> {
					attributes.put("message", messageInput.getText());	
						
					ActivityTO preformActivity = new ActivityTO(user.getPlayground(), currentElement.getPlayground(), currentElement.getId(), "postMessage", user.getPlayground(), user.getEmail(), attributes);
						String activityMessage = this.restTemplate.postForObject(
								this.url + "/{userPlayground}/{email}", 
								preformActivity,
								String.class, 
								user.getPlayground(),
								user.getEmail());
						
						new AlertBox("Activity was performed ",
								activityMessage.replaceAll("\"", ""));
						
						UserTO updatedUser = this.restTemplate.getForObject(
								ClientApplication.url+"/users/login/{playground}/{email}", 
								UserTO.class, 
								user.getPlayground(),
								user.getEmail());
								
								
						ClientApplication.afterLogin(updatedUser);
						stage.close();
					});

					GridPane.setConstraints(message, 0, 2);
					GridPane.setConstraints(messageInput, 1, 2);
					GridPane.setConstraints(actionBt, 0, 3);

					stage.setHeight(250);

					mainLayout.getChildren().remove(5, mainLayout.getChildren().size());
					mainLayout.getChildren().addAll(message, messageInput);
				} else if (activityValue.getValue().equals("Read all messages")) {
					message.setVisible(false);
					messageInput.setVisible(false);
					speed.setVisible(false);
					speedInput.setVisible(false);
					color.setVisible(false);
					colorInput.setVisible(false);
					actionBt.setVisible(true);

					actionBt.setText("Show messages");
					
					actionBt.setOnAction(a -> {							
						ActivityTO preformActivity = new ActivityTO(user.getPlayground(), currentElement.getPlayground(), currentElement.getId(), "readMessages", user.getPlayground(), user.getEmail(), attributes);
							String activityMessage = this.restTemplate.postForObject(
									this.url + "/{userPlayground}/{email}", 
									preformActivity,
									String.class, 
									user.getPlayground(),
									user.getEmail());
							activityMessage = activityMessage.replaceAll("\"", "");
							activityMessage = activityMessage.replaceAll("\\{", "");
							activityMessage = activityMessage.replaceAll("\\}", "");
							activityMessage = activityMessage.replaceAll(",", "\n\n");
							
							new AlertBox("Activity was performed ",
									activityMessage); 

							UserTO updatedUser = this.restTemplate.getForObject(
									ClientApplication.url+"/users/login/{playground}/{email}", 
									UserTO.class, 
									user.getPlayground(),
									user.getEmail());
									
									
							ClientApplication.afterLogin(updatedUser);
							stage.close();
						});

					GridPane.setConstraints(actionBt, 0, 2);

					stage.setHeight(200);

					mainLayout.getChildren().remove(5, mainLayout.getChildren().size());
				} else if (activityValue.getValue().equals("Spin")) {
					message.setVisible(false);
					messageInput.setVisible(false);
					speed.setVisible(true);
					speedInput.setVisible(true);
					color.setVisible(false);
					colorInput.setVisible(false);
					actionBt.setVisible(true);

					actionBt.setText("Spin!");

					actionBt.setOnAction(a -> {
						attributes.put("speed", speedInput.getValue());	
							
						ActivityTO preformActivity = new ActivityTO(user.getPlayground(), currentElement.getPlayground(), currentElement.getId(), "spin", user.getPlayground(), user.getEmail(), attributes);
							String activityMessage = this.restTemplate.postForObject(
									this.url + "/{userPlayground}/{email}", 
									preformActivity,
									String.class, 
									user.getPlayground(),
									user.getEmail());
							
							new AlertBox("Activity was performed ",
									activityMessage.replaceAll("\"", "")); 

							UserTO updatedUser = this.restTemplate.getForObject(
									ClientApplication.url+"/users/login/{playground}/{email}", 
									UserTO.class, 
									user.getPlayground(),
									user.getEmail());
									
									
							ClientApplication.afterLogin(updatedUser);
							stage.close();
						});

					GridPane.setConstraints(speed, 0, 2);
					GridPane.setConstraints(speedInput, 1, 2);

					stage.setHeight(200);

					mainLayout.getChildren().remove(5, mainLayout.getChildren().size());
					mainLayout.getChildren().addAll(speed, speedInput);
				} else if (activityValue.getValue().equals("Change Color")) {
					message.setVisible(false);
					messageInput.setVisible(false);
					speed.setVisible(false);
					speedInput.setVisible(false);
					color.setVisible(true);
					colorInput.setVisible(true);
					actionBt.setVisible(true);

					actionBt.setText("Change color");

					actionBt.setOnAction(a -> {
						attributes.put("color", colorInput.getValue());	
							
						ActivityTO preformActivity = new ActivityTO(user.getPlayground(), currentElement.getPlayground(), currentElement.getId(), "changeColor", user.getPlayground(), user.getEmail(), attributes);
							String activityMessage = this.restTemplate.postForObject(
									this.url + "/{userPlayground}/{email}", 
									preformActivity,
									String.class, 
									user.getPlayground(),
									user.getEmail());
							
							new AlertBox("Activity was performed ",
									activityMessage.replaceAll("\"", "")); 

							UserTO updatedUser = this.restTemplate.getForObject(
									ClientApplication.url+"/users/login/{playground}/{email}", 
									UserTO.class, 
									user.getPlayground(),
									user.getEmail());
									
									
							ClientApplication.afterLogin(updatedUser);
							stage.close();
						});

					GridPane.setConstraints(color, 0, 2);
					GridPane.setConstraints(colorInput, 1, 2);

					stage.setHeight(200);

					mainLayout.getChildren().remove(5, mainLayout.getChildren().size());
					mainLayout.getChildren().addAll(color, colorInput);
				}
			}
		});

		GridPane.setConstraints(element, 0, 0);
		GridPane.setConstraints(elementValue, 1, 0);
		GridPane.setColumnSpan(elementValue, 3);
		GridPane.setConstraints(activity, 0, 1);
		GridPane.setConstraints(activityValue, 1, 1);
		GridPane.setColumnSpan(activityValue, 3);
		GridPane.setConstraints(actionBt, 0, 3);

		mainLayout.getChildren().addAll(element, elementValue, activity, activityValue, actionBt);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setWidth(500);
		stage.setHeight(200);
		stage.setScene(scene);
		stage.showAndWait();

	}
}
