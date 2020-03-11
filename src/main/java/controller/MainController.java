package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import service.ConversionService;

import java.io.File;

public class MainController {

    @FXML
    private Label selectedFileLabel;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button convertXlsButton;

    private FileChooser fileChooser;
    private File selectedFile;
    private ConversionService conversionService;

    @FXML
    public void initialize() {
        convertXlsButton.setDisable(true);
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XLS files (*.xls, *.xlsx)", "*.xls", "*.xlsx"));
        conversionService = new ConversionService();
    }

    public void onSelectFileButtonClicked(ActionEvent event) {
        if (event.getSource().equals(selectFileButton)) {
            File selectedFile = fileChooser.showOpenDialog(selectFileButton.getScene().getWindow());
            if (selectedFile != null) {
                selectedFileLabel.setText(selectedFile.getAbsolutePath() + "  selected");
                convertXlsButton.setDisable(false);
                this.selectedFile = selectedFile;
            }
        }
    }

    public void onConvertXlsButtonClicked(ActionEvent event) {
        if (event.getSource().equals(convertXlsButton)) {
            conversionService.convert(selectedFile);
        }
    }
}
