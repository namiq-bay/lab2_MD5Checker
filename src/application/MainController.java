package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class MainController {
	@FXML
	private Button has_btn;

	@FXML
	private Button chose_btn;

	@FXML
	private ListView display;

	@FXML
	private Label passedLabel;

	@FXML
	private Label failedLabel;

	private String checksum;

	private File selectedFile;

	private File md5File;

	public void choseAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("C:/Users/namiq/Desktop"));
		selectedFile = fc.showOpenDialog(null);

		if (selectedFile != null) {
			display.getItems().add("File name: " + selectedFile.getName());

		} else {
			display.getItems().add("File is not valid or can not be added!");
		}

		try {

			File file = new File(selectedFile.getAbsolutePath());

			MessageDigest md5Digest = MessageDigest.getInstance("MD5");

			checksum = getFileChecksum(md5Digest, file);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addMD5Action(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("C:/Users/namiq/Desktop"));
		md5File = fc.showOpenDialog(null);

		if (md5File != null) {
			display.getItems().add("File name: " + md5File.getName());

		} else {
			display.getItems().add("File is not valid or cannot be added!");
		}
	}

	public void checkAction(ActionEvent event) {
		BufferedReader br;
		FileReader fr;
		String currentHash = "";

		if (md5File != null) {
			try {

				fr = new FileReader(md5File);
				br = new BufferedReader(fr);

				char[] a = new char[32];
				String str;

				try {
					br.read(a, 0, 32);

					for (char c : a)
						currentHash += c;

				} catch (IOException e) {
					display.getItems().add("The file cannot be read!");
				}

				if (currentHash.equals(checksum)) {
					passedLabel.setVisible(true);
					failedLabel.setVisible(false);
				} else {
					failedLabel.setVisible(true);
					passedLabel.setVisible(false);
				}

			} catch (FileNotFoundException e) {
				display.getItems().add("The file not found!");
			}
		}
	}

	public void hashAction(ActionEvent event) {

		display.getItems().add("Hash code: " + checksum);

	}

	public void clearAction(ActionEvent event) {
		display.getItems().clear();
	}

	public void saveAction(ActionEvent event) {
		PrintWriter writer;

		if (checksum != null) {
			try {
				File file = new File("C:/Users/namiq/Desktop/CHECKSUM.md5");
				writer = new PrintWriter(file, "UTF-8");
				writer.println(checksum + " - " + selectedFile.getName());
				writer.close();

				display.getItems().add("The file has been saved successfully!");
			} catch (Exception e) {
				display.getItems().add("File can not saved!");
			}
		} else {
			display.getItems().add("Hash the file!");
		}
	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {

		FileInputStream fis = new FileInputStream(file);

		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		fis.close();

		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

}
