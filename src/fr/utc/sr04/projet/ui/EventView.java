package fr.utc.sr04.projet.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import fr.utc.sr04.projet.model.Event;

public class EventView extends GridPane {

	@FXML
	public Label valueLabel;
	@FXML
	public Label dateLabel;
	@FXML
	public Label ownerLabel;
	
	
	public EventView(Event e) {
		FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/event.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        ownerLabel.setText(e.owner);
        valueLabel.setText(e.value);
        dateLabel.setText(e.time+"");
        
	}

	
}
