package VersionWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * 		WriteVersionToFile() method
 * 		@version: 1.0-beta
 * 		@author: Derek Sappington
 * 		@description:  Writes the version to file. 
 * 
 */

public class WriteVersionToFile {
	public void Writer(Stage stage, LinkedList<String> enumeratedVersion) {
		FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveVersionToFile(enumeratedVersion, file);
        }

	}

	private void saveVersionToFile(LinkedList<String> enumeratedVersion, File file) {
        @SuppressWarnings("unused")
		Alert alert;
		try {
            PrintWriter writer;
			writer = new PrintWriter(file);
			
			for (int i = 0; i < enumeratedVersion.size(); i++) {
				writer.println(enumeratedVersion.get(i));
				System.out.println(enumeratedVersion.get(i));
			}
			writer.close();
			alert = new Alert(AlertType.ERROR, "File saved");

		} catch (FileNotFoundException e) {
			alert = new Alert(AlertType.ERROR, "File did not saved");
			e.printStackTrace();
		}
	}
	// OLDER WRITER		
//	JFileChooser savefile = new JFileChooser();
//	savefile.setSelectedFile(new File(filename));
//	BufferedWriter writer;
//	int sf = savefile.showSaveDialog(savefile);
//
//	if (sf == JFileChooser.APPROVE_OPTION) {
//		try {
//			writer = new BufferedWriter(new FileWriter(savefile.getSelectedFile()));
//
//			for (int i = 0; i < enumeratedVersion.size(); i++) {
//
//				String temp = enumeratedVersion.get(i);
//				System.out.println(temp);
//				writer.write(temp);
//				writer.newLine();
//			}
//			writer.close();
//			JOptionPane.showMessageDialog(null, "File has been saved", "File Saved",
//					JOptionPane.INFORMATION_MESSAGE);
//			// true for rewrite, false for override
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	} else if (sf == JFileChooser.CANCEL_OPTION) {
//		JOptionPane.showMessageDialog(null, "File save has been canceled");
//	}
}