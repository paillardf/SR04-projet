package fr.utc.sr04.projet.ui;

import javafx.scene.control.ListCell;
import fr.utc.sr04.projet.model.EventsStack;

public class InformationListCell extends ListCell<EventsStack> {

	private MainController controller;

	public InformationListCell(MainController controller) {
		this.controller = controller;
	}

	@Override
	protected void updateItem(EventsStack info, boolean bln) {
		super.updateItem(info, bln);
		if (info != null) {
			setGraphic(new InformationView(info, controller));
		}
	}

}
