package playground.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import playground.layout.UserTO;

public class ClientApplication extends Application {

	public static Stage window;
	public static Scene startScene;
	public static String url;

	public static void main(String[] args) {
		// -Dplayground.host=192.168.5.108 -Dplayground.port=8083
		String host = System.getProperty("playground.host");
		if (host == null) {
			host = "localhost";
		}
		int port;
		try {
			port = Integer.parseInt(System.getProperty("playground.port"));
		} catch (Exception e) {
			port = 8083;
		}
		init(host,port);
		launch(args);

	}
	
	private static void init(String host, int port) {
		url = "http://" + host + ":" + port + "/playground";
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		BorderPane mainWindow = new BorderPane();
		mainWindow.getStyleClass().add("pane");
		VBox topLayout = new VBox(8);
		topLayout.setAlignment(Pos.CENTER);
		topLayout.paddingProperty().set(new Insets(10));;
		HBox buttonsLayout = new HBox(8);
		buttonsLayout.setAlignment(Pos.CENTER);

		Label welcome = new Label("Welcome to IOA Playground!");
		welcome.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
		Label whatToDo = new Label("What would you like to do?");
		whatToDo.setFont(Font.font("Verdana", FontPosture.ITALIC, 18));
		topLayout.getChildren().addAll(welcome, whatToDo);

		Button login = new MyButton("Login");
		login.setOnAction(e -> new LoginWindow());
		Button register = new MyButton("Register");
		register.setOnAction(e -> new RegisterWindow());
		buttonsLayout.getChildren().addAll(login, register);

		mainWindow.setTop(topLayout);
		mainWindow.setCenter(buttonsLayout);

		startScene = new Scene(mainWindow, 580, 150);
		startScene.getStylesheets().add("styles.css");
		window.setTitle("IOA Playground");
		window.getIcons().add(new Image("/fav.png"));
		window.setResizable(false);
		window.setScene(startScene);
		window.show();
	}

	public static void afterLogin(UserTO user) {
		BorderPane mainWindow = new BorderPane();
		mainWindow.requestLayout();
		mainWindow.getStyleClass().add("pane");
		VBox topTotal = new VBox(8);
		topTotal.setAlignment(Pos.CENTER);
		HBox topLayout1 = new HBox(8);
		topLayout1.setAlignment(Pos.CENTER);
		topLayout1.paddingProperty().set(new Insets(10,0,0,0));
		HBox topLayout2 = new HBox(8);
		topLayout2.setAlignment(Pos.CENTER);
		HBox buttonsLayout = new HBox(8);
		buttonsLayout.setAlignment(Pos.CENTER);
		
		String urlimg = user.getAvatar();
		Image img = new Image("/fav.png");
		try {
			img = new Image(urlimg,30,30,false,false);
		} catch (IllegalArgumentException ex) {
			img = new Image("/fav.png",30,30,false,false);
		}
		ImageView userImg = new ImageView();
		userImg.setImage(img);
		Label userWelcome = new Label("Welcome, " + user.getUsername() + "!");
		userWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
		Label userInfo = new Label("(Role:"+user.getRole()+"/Points:"+user.getPoints()+")");
		userInfo.setFont(Font.font("Verdana", FontPosture.ITALIC, 18));
		Button logout = new MyButton("Logout");
		topLayout1.getChildren().addAll(userImg, userWelcome, logout);
		topLayout2.getChildren().add(userInfo);
		topTotal.getChildren().addAll(topLayout1,topLayout2);
		
		Button updateProfile = new MyButton("Update Profile");
		Button addElement = new MyButton("Add Element");
		Button updateElement = new MyButton("Update Element");
		Button showElements = new MyButton("Show Elements");
		Button activeElement = new MyButton("Active Element");
		
		updateProfile.setOnAction(e -> {
			new UpdateProfileWindow(user);
		});
		
		addElement.setOnAction(e -> {
			new AddElementWindow(user);
		});
		
		updateElement.setOnAction(e -> {
			new UpdateElementWindow(user);
		});
		
		showElements.setOnAction(e -> {
			new ShowElementsWindow(user);
		});
		
		activeElement.setOnAction(e -> {
			new ActiveElementWindow(user);
		});
		
		logout.setOnAction(e -> {
			window.setScene(startScene);
		});
		

		buttonsLayout.getChildren().addAll(updateProfile, showElements);

		if (user.getRole().equals(UserTO.adminRole.toString())) {
			buttonsLayout.getChildren().addAll(addElement, updateElement);
		} else {
			buttonsLayout.getChildren().addAll(activeElement);
		}

		mainWindow.setTop(topTotal);
		mainWindow.setCenter(buttonsLayout);

		Scene scene = new Scene(mainWindow, 580, 150);
		scene.getStylesheets().add("styles.css");
		window.setScene(scene);
		window.setTitle(user.getUsername() + " - IOA Playground");

	}

	public static class MyButton extends Button {
		public MyButton(String text) {
			super(text);
			this.setMinSize(75, 25);
		}
	}

}
