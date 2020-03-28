package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.F1102Type;
import service.ConversionService;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private Label selectedFileLabel;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button convertXlsButton;
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField cifTextField;
    @FXML
    private ComboBox<String> sectorComboBox;
    @FXML
    private RadioButton yesRadioButton;
    @FXML
    private RadioButton noRadioButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField sumTextField;

    private FileChooser fileChooser;
    private File selectedFile;
    private ConversionService conversionService;

    @FXML
    public void initialize() {
        convertXlsButton.setDisable(true);
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XLS files (*.xls, *.xlsx)", "*.xls", "*.xlsx"));
        yearComboBox.getItems().setAll(getYears());
        monthComboBox.getItems().setAll(getMonths());
        sectorComboBox.getItems().setAll(getSectors());
        yearComboBox.getSelectionModel().select(String.valueOf(LocalDate.now().getYear()));
        monthComboBox.getSelectionModel().select(String.valueOf(LocalDate.now().getMonthValue()));
        sectorComboBox.getSelectionModel().select(1);
        noRadioButton.setSelected(true);
        setDatePicker();
        nameTextField.setText("CANTINA DE AJUTOR SOCIAL SI PENSIUNE");
        cifTextField.setText("5120733");
        sumTextField.setText("0");
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
            if(checkAllFields()) {
                conversionService.convert(selectedFile, getType());
            }
        }
    }

    public void onYesRadioButtonClicked(ActionEvent event) {
        if (event.getSource().equals(yesRadioButton)) {
            noRadioButton.setSelected(false);
        }
    }

    public void onNoRadioButtonClicked(ActionEvent event) {
        if (event.getSource().equals(noRadioButton)) {
            yesRadioButton.setSelected(false);
        }
    }

    private List<String> getYears() {
        List<String> years = new ArrayList<>();
        years.add(String.valueOf(LocalDate.now().minusYears(1).getYear()));
        years.add(String.valueOf(LocalDate.now().getYear()));
        return years;
    }

    private List<String> getMonths() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
    }

    private List<String> getSectors() {
        List<String> sectors = new ArrayList<>();
        sectors.add("01 - Buget");
        sectors.add("02 - Buget local");
        return sectors;
    }

    private boolean checkAllFields() {
        return true;
    }

    private F1102Type getType() {
        F1102Type f1102Type = new F1102Type();
        f1102Type.setAn(Integer.parseInt(yearComboBox.getValue()));
        f1102Type.setLunaR(Integer.parseInt(monthComboBox.getValue()));
        f1102Type.setCuiIp(cifTextField.getText());
        f1102Type.setDataDocument(datePicker.getValue().toString());
        f1102Type.setNumeIp(nameTextField.getText());
        f1102Type.setDRec(sectorComboBox.getSelectionModel().getSelectedIndex());
//        f1102Type.setSumaControl(Integer.parseInt(sumTextField.getText()));
        return f1102Type;
    }

    private void setDatePicker() {
        datePicker.setValue(LocalDate.now());
        datePicker.setEditable(false);
        datePicker.setConverter(new StringConverter<>()
        {
            private DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }
}
