package fr.utc.sr04.projet.ui;

import javafx.scene.control.ListCell;
import fr.utc.sr04.projet.model.Event;

public class EventListCell extends ListCell<Event> {

	@Override
	protected void updateItem(Event e, boolean bln) {
		super.updateItem(e, bln);
		if (e != null) {
			setGraphic(new EventView(e));
		}
	}

}
