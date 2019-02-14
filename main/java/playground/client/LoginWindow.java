package playground.client;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import playground.layout.UserTO;

public class LoginWindow {
	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users/login";

	private String playground = "2019A.itaikeren";
	
	private Stage stage;
	private Scene scene;
	private Label email = new Label("Email");
	private TextField emailInput = new TextField();
	private Button loginBt = new Button("Login");
	
	public LoginWindow() {
		stage = new Stage();
		stage.setTitle("Login");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		// Design inputs
		emailInput.setPromptText("example@email.com");

		GridPane.setConstraints(email, 0, 0);
		GridPane.setConstraints(emailInput, 1, 0);
		GridPane.setConstraints(loginBt, 0, 1);

		mainLayout.getChildren().addAll(email, emailInput, loginBt);
		
		loginBt.setOnAction(e -> {
			if(!emailInput.getText().isEmpty()) {
				try {
					UserTO loginUser = this.restTemplate.getForObject(
							this.url + "/{playground}/{email}", 
							UserTO.class, 
							playground,
							emailInput.getText());
					ClientApplication.afterLogin(loginUser);
					stage.close();
				} catch (HttpServerErrorException ex) {
					new AlertBox("Your email has not been verified",
							"This Email has not been verified, please verified first");
					
					stage.setScene(new ConfirmWindow().getSence(emailInput.getText(), playground));
					stage.setTitle("Confirm Code");
					
				} catch (Exception ex) {
					new AlertBox("Error", ex.getCause().getMessage());
				}
				
				
			}
		});
		
		loginBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
