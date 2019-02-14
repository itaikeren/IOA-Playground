package playground.client;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class UpdateElementWindow {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/elements";

	private Stage stage;
	private Scene scene;
	private Label element = new Label("Element");
	private ComboBox<String> elementValue = new ComboBox<>();
	private Label location = new Label("Location");
	private TextField xInput = new TextField();
	private TextField yInput = new TextField();
	private Label name = new Label("Name");
	private TextField nameInput = new TextField();
	private Label expirationDate = new Label("Expiration Date");
	private DatePicker expirationDateInput = new DatePicker();
	private CheckBox noExpirationDate = new CheckBox("No expiration date");
	private Label type = new Label("Type");
	private Label typeValue = new Label();
	private Button updateBt = new Button("Update Element");
	private ArrayList<ElementTO> allElements = new ArrayList<>();
	private ElementTO currentElement = new ElementTO();
	
	public UpdateElementWindow(UserTO user) {
		stage = new Stage();
		stage.setTitle("Update Element");
		stage.getIcons().add(new Image("/fav.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode elements = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/all",
				JsonNode.class,
				user.getPlayground(),
				user.getEmail());
		
		try {
			allElements = mapper.readValue(
				    mapper.treeAsTokens(elements), 
				    new TypeReference<ArrayList<ElementTO>>(){}
				);
		} catch (JsonParseException ex) {
			new AlertBox("Error", ex.getMessage());
		} catch (JsonMappingException ex) {
			new AlertBox("Error", ex.getMessage());
		} catch (IOException ex) {
			new AlertBox("Error", ex.getMessage());
		}
		
		// Design inputs
		for(ElementTO e : allElements) {
			elementValue.getItems().add(e.getName()+"(#"+e.getId()+")");
		}
		expirationDateInput.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		
		xInput.setDisable(true);
		yInput.setDisable(true);
		nameInput.setDisable(true);
		expirationDateInput.setDisable(true);
		noExpirationDate.setDisable(true);
		typeValue.setDisable(true);
		updateBt.setMinWidth(150);
		updateBt.setDisable(true);

		elementValue.setOnAction(e -> {
			xInput.setDisable(false);
			yInput.setDisable(false);
			nameInput.setDisable(false);
			expirationDateInput.setDisable(false);
			noExpirationDate.setDisable(false);
			updateBt.setDisable(false);
			
			ElementTO selectedElement = new ElementTO();
			for (ElementTO et : allElements) {
				String check = et.getName()+"(#"+et.getId()+")";
				if (check.equals(elementValue.getValue())) {
					selectedElement = et;
				}
			}
			
			currentElement = this.restTemplate.getForObject(
					this.url + "/{userPlayground}/{email}/{playground}/{id}",
					ElementTO.class,
					user.getPlayground(),
					user.getEmail(),
					selectedElement.getPlayground(),
					selectedElement.getId());
			
			xInput.setText(currentElement.getLocation().getX()+"");
			yInput.setText(currentElement.getLocation().getY()+"");
			nameInput.setText(currentElement.getName());
			expirationDateInput.setValue(dateToLocalDate(currentElement.getExpirationDate()));
			typeValue.setText(currentElement.getType());
		});
		
		GridPane.setConstraints(element, 0, 0);
		GridPane.setConstraints(elementValue, 1, 0);
		GridPane.setConstraints(location, 0, 1);
		GridPane.setConstraints(xInput, 1, 1);
		GridPane.setConstraints(yInput, 2, 1);
		GridPane.setConstraints(name, 0, 2);
		GridPane.setConstraints(nameInput, 1, 2);
		GridPane.setConstraints(expirationDate, 0, 3);
		GridPane.setConstraints(expirationDateInput, 1, 3);
		GridPane.setConstraints(noExpirationDate, 2, 3);
		GridPane.setConstraints(type, 0, 4);
		GridPane.setConstraints(typeValue, 1, 4);
		GridPane.setConstraints(updateBt, 0, 5);

		noExpirationDate.setOnAction(e -> {
			if (noExpirationDate.isSelected()) {
				expirationDateInput.setDisable(true);
			} else {
				expirationDateInput.setDisable(false);
			}
		});

		updateBt.setOnAction(e -> {
			if(expirationDateInput.getValue() == null && !noExpirationDate.isSelected()) {
				new AlertBox("Error", "Please choose expiration date or check the box");
			} else {				
				Date ed = new Date();
				if(!noExpirationDate.isSelected()) {
					ed = localDateToDate(expirationDateInput.getValue());
				} else {
					ed = null;
				}
				try {
					ElementTO updatedElement = new ElementTO(new Location(Double.parseDouble(xInput.getText()),
							Double.parseDouble(yInput.getText())),
							nameInput.getText(),
							ed,
							typeValue.getText(),
							currentElement.getAttributes(),
							currentElement.getCreatorPlayground(),
							currentElement.getCreatorEmail());

					this.restTemplate.put(
							this.url + "/{userPlayground}/{email}/{playground}/{id}", 
							updatedElement, 
							currentElement.getPlayground(),
							currentElement.getCreatorEmail(),
							currentElement.getCreatorPlayground(),
							currentElement.getId());
					new AlertBox("Update element successfully",
							"You have successfully update the element!");
					stage.close();
						
				} catch (NumberFormatException ex) {
					new AlertBox("Only Numbers", "Only numbers in X and Y!!!");
				} catch (Exception ex) {
					new AlertBox("Error", ex.getMessage());
				}
				
			}
		});
		
		mainLayout.getChildren().addAll(element, elementValue, location, xInput, yInput, name, nameInput, expirationDate, expirationDateInput,
				noExpirationDate, type, typeValue, updateBt);

		updateBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	private Date localDateToDate(LocalDate localDate) {
		if(localDate != null) {
			Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
			return Date.from(instant);
		}
		return null;
	}
	
	private LocalDate dateToLocalDate(Date date) {
		if(date != null) {
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			return localDate;
		}
		return null;
	}
}
