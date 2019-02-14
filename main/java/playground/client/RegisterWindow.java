package playground.client;

import org.springframework.web.client.HttpServerErrorException;
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
import playground.layout.NewUserForm;
import playground.layout.UserTO;

public class RegisterWindow {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users";

	private Stage stage;
	private Scene scene;
	private Label mail = new Label("Email");
	private TextField mailInput = new TextField();
	private Label userName = new Label("Username");
	private TextField userNameInput = new TextField();
	private Label role = new Label("Role");
	private ComboBox<String> roleInput = new ComboBox<>();
	private Label avatar = new Label("Avatar");
	private TextField avatarInput = new TextField();
	private Button registerBt = new Button("Register");

	public RegisterWindow() {

		stage = new Stage();
		stage.setTitle("Register");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		// Design inputs
		mailInput.setPromptText("example@email.com");
		userNameInput.setPromptText("myUser");
		roleInput.getItems().addAll(UserTO.adminRole, UserTO.userRole);
		avatarInput.setPromptText("myAvatar");

		GridPane.setConstraints(mail, 0, 0);
		GridPane.setConstraints(mailInput, 1, 0);
		GridPane.setConstraints(userName, 0, 1);
		GridPane.setConstraints(userNameInput, 1, 1);
		GridPane.setConstraints(role, 0, 2);
		GridPane.setConstraints(roleInput, 1, 2);
		GridPane.setConstraints(avatar, 0, 3);
		GridPane.setConstraints(avatarInput, 1, 3);
		GridPane.setConstraints(registerBt, 1, 4);

		mainLayout.getChildren().addAll(mail, mailInput, userName, userNameInput, role, roleInput, avatar, avatarInput,
				registerBt);
		
		registerBt.setOnAction(e -> {

			if (!mailInput.getText().isEmpty() && !userName.getText().isEmpty()
					&& !roleInput.getValue().isEmpty() && !avatarInput.getText().isEmpty()) {

				NewUserForm user = new NewUserForm(mailInput.getText(), userNameInput.getText(), avatarInput.getText(),
						roleInput.getValue().toString());
				try {
					UserTO newUser = restTemplate.postForObject(url,
							new UserTO(user.getEmail(), user.getUsername(), user.getRole(), user.getAvatar()),
							UserTO.class);
					new AlertBox("Registered successfully",
							"You have successfully registered! We email you the confrimation code.");

					stage.setScene(new ConfirmWindow().getSence(newUser.getEmail(), newUser.getPlayground()));
					stage.setTitle("Confirm Code");

				} catch (HttpServerErrorException ex) {
					new AlertBox("Email already in use",
							"This Email is already in use, please try to login or use new Email address");
				} catch (Exception ex) {
					new AlertBox("Error", ex.getCause().getMessage());
				}

			}
		});

		registerBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();

	}
}
