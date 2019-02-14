package playground.client;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import playground.layout.UserTO;

public class ConfirmWindow {
	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users/confirm";

	private Scene scene;
	private String email;
	private String playground;
	private Label code = new Label("Confirm Code");
	private TextField codeInput = new TextField();
	private Button confirmBt = new Button("Confirm");
	
	
	public ConfirmWindow() {
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);
		
		GridPane.setConstraints(code, 0, 0);
		GridPane.setConstraints(codeInput, 1, 0);
		GridPane.setConstraints(confirmBt, 0, 1);
		
		mainLayout.getChildren().addAll(code,codeInput,confirmBt);
		
		confirmBt.setDefaultButton(true);
		
		confirmBt.setOnAction(e -> {
			if(!codeInput.getText().isEmpty()) {
				try {
					this.restTemplate.getForObject(
							this.url + "/{playground}/{email}/{code}", 
							UserTO.class, 
							playground,
							email,
							Integer.parseInt(codeInput.getText().toString()));
					new AlertBox("Successfully Confirm", "You have successfully confirmed! Go login now!");
					((Stage) scene.getWindow()).close();
					
				} catch (HttpServerErrorException ex) {
					new AlertBox("Invalid Code",
							"Your code is invalid, please check your Email address again and re-try");
				} catch (Exception ex) {
					new AlertBox("Error", ex.getMessage());
				}
			}
		});
		
		scene = new Scene(mainLayout);
	}
	
	public Scene getSence(String email, String playground) {
		this.email = email;
		this.playground = playground;
		
		return this.scene;
	}
}
