package App;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import APICaller.GitHubAPICall;
import Crawler.Spider;
import VersionWriter.WriteVersionToFile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.MenuItem;
import java.awt.PopupMenu;

/*
 * 		Main() method
 * 		@version: 1.0-beta
 * 		@author: Derek Sappington
 * 		@description: Set up Application stage, Formats System tray, handles user action.  
 * 
 */

public class App extends Application {

	protected Stage stage; // stage
	Pattern pattern = Pattern.compile("\\s"); // catches urls with whitespaces
	Matcher matcher; // matches version to regex

	final String SystemTrayiconImage = "http://icons.iconarchive.com/icons/designbolts/flat-social-media/16/Github-icon.png"; // system tray icon
	
	MenuItem scrapeItem = new MenuItem("Scrape"); // Scrape MenuItem
	MenuItem githubAPIItem = new MenuItem("Quick Scrape"); // Quick Search MenuItem
	MenuItem aboutItem = new MenuItem("About"); // About MenuItem
	MenuItem exitItem = new MenuItem("Exit"); // Exit MenuItem
	final PopupMenu popup = new PopupMenu(); // puts MenuItem into PopupMenu

	TextInputDialog dialog = new TextInputDialog();
	Alert alert;
	Optional<String> result;

	final String message = "Enter The URL:";
	LinkedList<String> finalList = new LinkedList<String>();
	WriteVersionToFile writer = new WriteVersionToFile();
	String filename;
	static String url;
    String fileName = "ReadMe.txt";


	Spider spider = new Spider(); // Calls Spider()
	GitHubAPICall callGitAPI = new GitHubAPICall(); // Calls GitHubAPICall()
	private BufferedReader bufferedReader;

	public static void main(String[] args) throws IOException, java.awt.AWTException {
		launch(args);
	}

	public void start(final Stage stage) {

		// stores a reference to the stage.
		this.stage = stage;

		// instructs the javafx system not to exit implicitly when the last application
		// window is shut.
		Platform.setImplicitExit(false);

		// sets up the tray icon (using awt code run on the swing thread).
		javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

		stage.initStyle(StageStyle.TRANSPARENT);

		StackPane layout = new StackPane(createContent());
		layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");

		layout.setPrefSize(300, 200);

		layout.setOnMouseClicked(event -> stage.hide());

		// a scene with a transparent fill is necessary to implement the translucent app
		// window.
		Scene scene = new Scene(layout);

		scene.setFill(Color.TRANSPARENT);

		stage.setScene(scene);
	}

	private Node createContent() {
		Label scrape = new Label("New Scrape");
		VBox content = new VBox(10, scrape);

		content.setAlignment(Pos.CENTER);

		return content;
	}

	private void addAppToTray() {
		try {
			// ensure awt toolkit is initialized.
			java.awt.Toolkit.getDefaultToolkit();

			// app requires system tray support, just exit if there is no support.
			if (!java.awt.SystemTray.isSupported()) {
				System.out.println("No system tray support, application exiting.");
				Platform.exit();
			}

			// set up a system tray icon.
			java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
			URL imageLoc = new URL(SystemTrayiconImage);
			java.awt.Image image = ImageIO.read(imageLoc);
			java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

			// Actionhandler for user selection on icon Quick selection
			trayIcon.addActionListener(event -> Platform.runLater(() -> {
				try {
					showScraper();
				} catch (AWTException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));

			// Call Scraper Handler select Scaper in popup
			scrapeItem.addActionListener(event -> Platform.runLater(() -> {
				try {
					showScraper();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}));

			githubAPIItem.addActionListener(event -> Platform.runLater(() -> {
				try {
					showGitHubAPI();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}));

			// Call About Handler in popup
			aboutItem.addActionListener(event -> Platform.runLater(() -> {
				try {
					showAbout();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}));

			// exits Platform in popup
			exitItem.addActionListener(event -> {
				Platform.exit();
				tray.remove(trayIcon);
			});

			// setup the popup menu for the application.
			popup.add(scrapeItem);
			popup.add(githubAPIItem);
			popup.add(aboutItem);
			popup.add(exitItem);
			popup.addSeparator();

			trayIcon.setPopupMenu(popup);

			// add the application tray icon to the system tray.
			tray.add(trayIcon);

		} catch (java.awt.AWTException | IOException e) {
			System.out.println("Unable to init system tray");
			e.printStackTrace();
		}

	}

	// Below starts Handlers

	private void showScraper() throws IOException, AWTException {
		if (this.stage != null) {

			dialog.setTitle("Basic Scrape");
			dialog.setHeaderText(message);
			dialog.setContentText("URL:");
			dialog.getDialogPane().setMinSize(300, 200);
			result = dialog.showAndWait();
			url = result.get();

			matcher = pattern.matcher(url);

			if (matcher.find() == true || url.length() <= 8) {
				alert = new Alert(AlertType.ERROR, "Invalid URL. \nCheck for whitespaces");
				alert.getDialogPane().setMinSize(300, 200);
				reset(); // resets dialog
				alert.showAndWait();
				return;
			}

			if (result.isPresent()) {
				// set up return and write to file here
				// ***************************************************************
				finalList = spider.Version_Spider(url);
				System.out.println(finalList.size());
				if (finalList.size() == 1) {
					alert = new Alert(AlertType.ERROR, "No Versions were scraped.");
					alert.getDialogPane().setMinSize(300, 200);
					reset(); // resets dialog
					alert.showAndWait();
					return;
				} else {
					reset();
					writer.Writer(stage, finalList); // writes to file
					reset();
					finalList.remove(); // clears out LinkedList with version
					return;
				}
			}
		}
	}

	private void showGitHubAPI() throws IOException, AWTException {

		if (this.stage != null) {

			dialog.setTitle("GitHub API Call");
			dialog.setHeaderText(message);
			dialog.setContentText("URL:");
			dialog.getDialogPane().setMinSize(300, 200);
			result = dialog.showAndWait();
			url = result.get();

			matcher = pattern.matcher(url);

			// makes sure user enter valid url without
			if (matcher.find() == true || url.length() <= 8) {
				alert = new Alert(AlertType.ERROR, "Invalid URL. \nCheck for whitespaces");
				alert.getDialogPane().setMinSize(300, 200);
				reset(); // resets dialog
				alert.showAndWait();
				return;
			}

			// Listens for User hitting ok or not
			if (result.isPresent()) {
				finalList = callGitAPI.callGitAPI(url);

				// Checks if there are any versions scraped.
				if (finalList.size() == 1) {
					alert = new Alert(AlertType.ERROR, "No Versions were scraped.");
					alert.getDialogPane().setMinSize(300, 200);
					reset(); // resets dialog
					alert.showAndWait();
					return;
				} else {
					reset();
					writer.Writer(stage, finalList); // writes to file
					reset();
					finalList.remove(); // clears out LinkedList with version
					return;
				}
			}
		}
	}

	private void showAbout() throws IOException, AWTException {
		 StringBuilder sb = new StringBuilder();
		 FileReader fileReader =  new FileReader(fileName);
         bufferedReader = new BufferedReader(fileReader);
         String line = bufferedReader.readLine();
		 while(line != null) {
			 sb.append(line);
		     sb.append(System.lineSeparator());
		     line = bufferedReader.readLine();
		 }
		 String about = sb.toString();

		 
		if (this.stage != null) {
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("GitHub Version Scraper");
			alert.setContentText(about);
			alert.getDialogPane().setMinSize(500, 200);
			alert.showAndWait();

		}
	}

	private void reset() {
		dialog.getEditor().clear();
		url = "";
	}

}
