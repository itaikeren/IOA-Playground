package playground.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import playground.layout.ElementTO;
import playground.layout.UserTO;

public class ShowElementsWindow {
	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/elements";

	private Stage stage;
	private Scene scene;
	private Label element = new Label("Element");
	private ComboBox<String> elementValue = new ComboBox<>();
	private Label id = new Label("ID");
	private Label idValue = new Label();
	private Label playground = new Label("Playground");
	private Label playgroundValue = new Label();
	private Label location = new Label("Location");
	private Label locationValue = new Label();
	private Label name = new Label("Name");
	private Label nameValue = new Label();
	private Label creationDate = new Label("Creation Date");
	private Label creationDateValue = new Label();
	private Label expirationDate = new Label("Expiration Date");
	private Label expirationDateValue = new Label();
	private Label type = new Label("Type");
	private Label typeValue = new Label();
	private Label attributes = new Label("Attributes");
	private Label attributesValue = new Label();
	private Label creatorPlayground = new Label("Creator Playground");
	private Label creatorPlaygroundValue = new Label();
	private Label creatorEmail = new Label("Creator Email");
	private Label creatorEmailValue = new Label();
	private Label color = new Label();
	private ArrayList<ElementTO> allElements = new ArrayList<>();
	private ElementTO currentElement = new ElementTO();

	public ShowElementsWindow(UserTO user) {
		stage = new Stage();
		stage.setTitle("Show Elements");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		ObjectMapper mapper = new ObjectMapper();

		JsonNode elements = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all", JsonNode.class,
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
			if(user.getRole().equals(UserTO.adminRole)) {
				elementValue.getItems().add(e.getName()+"(#"+e.getId()+")");				
			} else {
				if(e.getExpirationDate() != null) {
					if(!e.getExpirationDate().before(new Date())) {
						elementValue.getItems().add(e.getName()+"(#"+e.getId()+")");
					}					
				} else {
					elementValue.getItems().add(e.getName()+"(#"+e.getId()+")");
				}
			}
		}
		
		elementValue.setOnAction(e -> {

			ElementTO selectedElement = new ElementTO();
			for (ElementTO et : allElements) {
				String check = et.getName()+"(#"+et.getId()+")";
				if (check.equals(elementValue.getValue())) {
					selectedElement = et;
				}
			}

			currentElement = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}",
					ElementTO.class, user.getPlayground(), user.getEmail(), selectedElement.getPlayground(),
					selectedElement.getId());

			idValue.setText("#" + currentElement.getId());
			playgroundValue.setText(currentElement.getPlayground());
			locationValue.setText(
					"(" + currentElement.getLocation().getX() + "," + currentElement.getLocation().getY() + ")");
			nameValue.setText(currentElement.getName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy @ HH:mm");
			creationDateValue.setText(dateFormat.format(currentElement.getCreationDate()));
			if (currentElement.getExpirationDate() != null) {
				expirationDateValue.setText(dateFormat.format(currentElement.getExpirationDate()));
				expirationDateValue.setDisable(false);
				expirationDateValue.setStyle("-fx-font-weight: regular");
			} else {
				expirationDateValue.setText("No expiration date");
				expirationDateValue.setDisable(true);
				expirationDateValue.setStyle("-fx-font-style: italic");
			}
			typeValue.setText(currentElement.getType());
			creatorPlaygroundValue.setText(currentElement.getCreatorPlayground());
			creatorEmailValue.setText(currentElement.getCreatorEmail());
			attributesValue.setText(getAttributes(currentElement));
			attributesValue.setMaxWidth(270);
			attributesValue.setWrapText(true);
			if(currentElement.getType().equals("Carrousel")) {
				color.setVisible(true);
				color.setText("Color "+currentElement.getAttributes().get("color")+"");
				switch (currentElement.getAttributes().get("color")+"") {
				case "Orange":
					color.setStyle("-fx-background-color: DarkOrange;");					
					break;
					
				case "Blue":
					color.setStyle("-fx-background-color: LightSkyBlue;");					
					break;

				case "Red":
					color.setStyle("-fx-background-color: Crimson;");					
					break;
					
				case "Green":
					color.setStyle("-fx-background-color: LightGreen;");					
					break;
				}
				color.setMinWidth(55);
			} else {
				color.setVisible(false);
			}
			
		});

		GridPane.setConstraints(element, 0, 0);
		GridPane.setConstraints(elementValue, 1, 0);
		GridPane.setColumnSpan(elementValue, 3);
		GridPane.setConstraints(id, 0, 1);
		GridPane.setConstraints(idValue, 1, 1);
		GridPane.setConstraints(playground, 0, 2);
		GridPane.setConstraints(playgroundValue, 1, 2);
		GridPane.setConstraints(location, 0, 3);
		GridPane.setConstraints(locationValue, 1, 3);
		GridPane.setConstraints(name, 0, 4);
		GridPane.setConstraints(nameValue, 1, 4);
		GridPane.setConstraints(creationDate, 0, 5);
		GridPane.setConstraints(creationDateValue, 1, 5);
		GridPane.setConstraints(expirationDate, 0, 6);
		GridPane.setConstraints(expirationDateValue, 1, 6);
		GridPane.setConstraints(type, 0, 7);
		GridPane.setConstraints(typeValue, 1, 7);
		GridPane.setConstraints(creatorPlayground, 0, 8);
		GridPane.setConstraints(creatorPlaygroundValue, 1, 8);
		GridPane.setConstraints(creatorEmail, 0, 9);
		GridPane.setConstraints(creatorEmailValue, 1, 9);
		GridPane.setConstraints(attributes, 0, 10);
		GridPane.setConstraints(attributesValue, 1, 10);
		GridPane.setConstraints(color, 2, 10);
		
		mainLayout.getChildren().addAll(element, elementValue, id, idValue, playground, playgroundValue, location,
				locationValue, name, nameValue, creationDate, creationDateValue, expirationDate, expirationDateValue,
				type, typeValue, creatorPlayground, creatorPlaygroundValue, creatorEmail, creatorEmailValue, attributes,
				attributesValue,color);

		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setWidth(500);
		stage.setScene(scene);
		stage.showAndWait();

	}

	private String getAttributes(ElementTO element) {
		String str = "";
		if (element.getType().equals("Message Board")) {
			if (!element.getAttributes().isEmpty()) {
				int counter = 0;
				attributes.setWrapText(true);
				
				for (Map.Entry<String, Object> entry : element.getAttributes().entrySet()) {
					str += entry.getKey() + ": " + entry.getValue() + "\n";
					counter++;
				}
				stage.setHeight(400 + (20*counter));
			} else {
				str += "No message on that borad yet!";
			}
		} else {
			stage.setHeight(400);
			str += "Speed : " + element.getAttributes().get("speed");
		}
		return str;
	}
}
