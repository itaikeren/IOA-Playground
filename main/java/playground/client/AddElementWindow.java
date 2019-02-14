package playground.client;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.layout.logic.Location;

public class AddElementWindow {
	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/elements";

	private Stage stage;
	private Scene scene;
	private Label location = new Label("Location");
	private TextField xInput = new TextField();
	private TextField yInput = new TextField();
	private Label name = new Label("Name");
	private TextField nameInput = new TextField();
	private Label expirationDate = new Label("Expiration Date");
	private DatePicker expirationDateInput = new DatePicker();
	private CheckBox noExpirationDate = new CheckBox("No expiration date");
	private Label type = new Label("Type");
	private ComboBox<String> typeInput = new ComboBox<>();
	private Button createBt = new Button("Create Element");

	public AddElementWindow(UserTO user) {
		stage = new Stage();
		stage.setTitle("Add Element");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		// Design inputs
		xInput.setPromptText("x");
		yInput.setPromptText("y");
		typeInput.getItems().addAll("Message Board", "Carrousel");
		createBt.setMinWidth(150);
		expirationDateInput.setDayCellFactory(picker -> new DateCell() {
		        public void updateItem(LocalDate date, boolean empty) {
		            super.updateItem(date, empty);
		            LocalDate today = LocalDate.now();
		            setDisable(empty || date.compareTo(today) < 0 );
		        }
		    });

		GridPane.setConstraints(location, 0, 0);
		GridPane.setConstraints(xInput, 1, 0);
		GridPane.setConstraints(yInput, 2, 0);
		GridPane.setConstraints(name, 0, 1);
		GridPane.setConstraints(nameInput, 1, 1);
		GridPane.setConstraints(expirationDate, 0, 2);
		GridPane.setConstraints(expirationDateInput, 1, 2);
		GridPane.setConstraints(noExpirationDate, 2, 2);
		GridPane.setConstraints(type, 0, 3);
		GridPane.setConstraints(typeInput, 1, 3);
		GridPane.setConstraints(createBt, 0, 4);

		noExpirationDate.setOnAction(e -> {
			if (noExpirationDate.isSelected()) {
				expirationDateInput.setDisable(true);
			} else {
				expirationDateInput.setDisable(false);
			}
		});

		createBt.setOnAction(e -> {
			if (!xInput.getText().isEmpty() && !yInput.getText().isEmpty() && !nameInput.getText().isEmpty()
					&& typeInput.getValue() != null) {
				if (expirationDateInput.getValue() == null && !noExpirationDate.isSelected()) {
					new AlertBox("Error", "Please choose expiration date or check the box");
				} else {
					try {
						Date ed = new Date();
						Map<String, Object> attributes = new HashMap<>();
						if (!noExpirationDate.isSelected()) {
							ed = localDateToDate(expirationDateInput.getValue());
						} else {
							ed = null;
						}
						if (typeInput.getValue().equals("Carrousel")) {
							attributes.put("speed", 0);
							attributes.put("color", "Orange");
						}
						ElementTO newElement = new ElementTO(
								new Location(Double.parseDouble(xInput.getText()),
										Double.parseDouble(yInput.getText())),
								nameInput.getText(), ed, typeInput.getValue(), attributes, user.getPlayground(),
								user.getEmail());

						this.restTemplate.postForObject(this.url + "/" + user.getPlayground() + "/" + user.getEmail(),
								newElement, ElementTO.class);

						new AlertBox("Add element successfully", "You have successfully add new element!");

						stage.close();
					} catch (NumberFormatException ex) {
						new AlertBox("Only Numbers", "Only numbers in X and Y!!!");
					} catch (Exception ex) {
						new AlertBox("Error", ex.getCause().getMessage());
					}
				}
			}
		});

		mainLayout.getChildren().addAll(location, xInput, yInput, name, nameInput, expirationDate, expirationDateInput,
				noExpirationDate, type, typeInput, createBt);

		createBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();

	}

	private Date localDateToDate(LocalDate localDate) {
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
}
