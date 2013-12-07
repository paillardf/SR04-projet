package fr.utc.sr04.projet;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.jconnect.JConnect;
import com.jconnect.impl.peergroup.NetPeerGroup;

import fr.utc.sr04.projet.data.DataBaseManager;
import fr.utc.sr04.projet.service.EventSyncService;
import fr.utc.sr04.projet.ui.MainController;

public class App extends Application {

	public static String prefFolder = "pref";
	/**
	 * @param args
	 */

	private JConnect jConnect;
	private NetPeerGroup peerGroup;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Handler consoleHandler = new ConsoleHandler();
		// consoleHandler.setLevel(Level.ALL);
		// Logger.getAnonymousLogger().addHandler(consoleHandler);

		// logger.config("config");
		jConnect = new JConnect(prefFolder);
		jConnect.getGate().start();
		peerGroup = (NetPeerGroup) jConnect.getPeerGroupManager()
				.newGroupInstance(NetPeerGroup.class,
						NetPeerGroup.NETPEERGROUP_UUID);
		
		
		peerGroup.addService(new EventSyncService(peerGroup));
		//
		
		//
		//
		//
		URL location = App.class.getResource(
				"ui/fxml/main.fxml");

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root = (Parent) fxmlLoader.load(location.openStream());
		MainController currentController = (MainController) fxmlLoader
				.getController();
		currentController.setBDD(new DataBaseManager(prefFolder));
		currentController.setPeerGroup(peerGroup);
		
		//
		Scene scene = new Scene(root, 470, 600);
		//
		stage.setTitle("Projet SR04: "+prefFolder);
		stage.setScene(scene);
		stage.show();
		jConnect.getPeerGroupManager().startAllGroup();

	}

	public void stop() throws Exception {
		super.stop();
		// mStage.close();
		jConnect.getGate().stop();
		jConnect.getPeerGroupManager().stopAllGroup();
		//
	}

}
