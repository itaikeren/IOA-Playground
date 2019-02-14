package playground.client;

import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import playground.layout.UserTO;

public class UpdateProfileWindow {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users";

	private String playground = "2019A.itaikeren";
	
	private Stage stage;
	private Scene scene;
	private Label email = new Label("Email");
	private Label emailValue = new Label();
	private Label role = new Label("Role");
	private ComboBox<String> roleInput = new ComboBox<>();
	private Label username = new Label("Usermame");
	private TextField usernameInput = new TextField();
	private Label avatar = new Label("Avatar");
	private TextField avatarInput = new TextField();
	private Label points = new Label("Points");
	private Label pointsValue = new Label();
	private Button updateBt = new Button("Update");
	
	public UpdateProfileWindow(UserTO user) {
		stage = new Stage();
		stage.setTitle("Update Profile");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		// Design inputs
		emailValue.setText(user.getEmail());
		emailValue.setDisable(true);
		roleInput.getItems().addAll(UserTO.adminRole, UserTO.userRole);
		roleInput.setValue(user.getRole());
		usernameInput.setText(user.getUsername());
		avatarInput.setText(user.getAvatar());
		pointsValue.setText(user.getPoints()+"");
		pointsValue.setDisable(true);
		
		GridPane.setConstraints(email, 0, 0);
		GridPane.setConstraints(emailValue, 1, 0);
		GridPane.setConstraints(role, 0, 2);
		GridPane.setConstraints(roleInput, 1, 2);
		GridPane.setConstraints(username, 0, 3);
		GridPane.setConstraints(usernameInput, 1, 3);
		GridPane.setConstraints(avatar, 0, 4);
		GridPane.setConstraints(avatarInput, 1, 4);
		GridPane.setConstraints(points, 0, 5);
		GridPane.setConstraints(pointsValue, 1, 5);
		GridPane.setConstraints(updateBt, 0, 6);

		mainLayout.getChildren().addAll(email, emailValue, role, roleInput, username, usernameInput, avatar, avatarInput, points, pointsValue, updateBt);
		
		updateBt.setOnAction(e -> {
			
			UserTO updatedUser = new UserTO(user.getEmail(),usernameInput.getText(), roleInput.getValue(), avatarInput.getText());
			updatedUser.setPoints(user.getPoints());
			this.restTemplate.put(
					this.url + "/{playground}/{email}", 
					updatedUser, 
					playground,
					user.getEmail());
			ClientApplication.afterLogin(updatedUser);
			stage.close();
		});
		
		updateBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setMinWidth(200);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
