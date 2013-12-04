package fr.utc.sr04.projet.ui;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import fr.utc.sr04.projet.model.Event;
import fr.utc.sr04.projet.model.EventsStack;

public class InformationView extends GridPane {

	@FXML
	public TextField valueField;
	@FXML
	public Label dateLabel;
	@FXML
	public Label ownerLabel;
	@FXML
	public VBox eventBox;

	private EventsStack info;
	private MainController controller;

	public InformationView(EventsStack info, MainController controller) {
		this.info = info;
		this.controller = controller;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"fxml/information.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		if (info.getEvents().size() > 0) {

			dateLabel
					.setText(info.getEvents().get(0).time
							+ "");
			valueField.setText(info.getEvents()
					.get(0).value);
			ownerLabel.setText(info.getEvents()
					.get(0).owner);

		}

		final ObservableList<Event> eventList = FXCollections
				.observableArrayList(info.getEvents());
		final ListView<Event> listView = new ListView<Event>();
		listView.setItems(eventList);
		listView.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
			@Override
			public ListCell<Event> call(ListView<Event> list) {
				return new EventListCell();
			}
		});
		eventBox.getChildren().add(listView);

		eventBox.visibleProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					InformationView.this.getChildren().remove(eventBox);
//					eventBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//					eventBox.setPrefSize(Double.MAX_VALUE, 100);
				} else {
					InformationView.this.getChildren().add(eventBox);
//					eventBox.setMaxSize(0, 0);
//					eventBox.setMinSize(0, 0);

				}
			}
		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() > 1) {
					eventBox.setVisible(!eventBox.visibleProperty().getValue());
					listView.requestLayout();//setItems(eventList);
				}

			}

		});
		
		//eventBox.setVisible(false);
		// set style
		// this.setAlignment(Pos.CENTER_LEFT);
		// this.setPrefWidth(WIDTH);
		// this.setPrefHeight(70);
		// this.getStyleClass().addAll("simpleCard");
	}

	@FXML
	public void handleTextEntered(ActionEvent actionEvent) {
		
		controller.createNewEvent(info, valueField.getText());
		

	}
}
