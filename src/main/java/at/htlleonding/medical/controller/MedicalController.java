package at.htlleonding.medical.controller;

import at.htlleonding.medical.model.ChangeObserver;
import at.htlleonding.medical.model.Patient;
import at.htlleonding.medical.model.SerializeTool;
import at.htlleonding.medical.model.WaitingRoom;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MedicalController {

    @FXML
    private TextField name, time;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox emergency;
    @FXML
    private Label treatment, preparation, leftPatient;
    @FXML
    private TextArea textArea;
    @FXML
    private Button nextButton;

    private WaitingRoom room = new WaitingRoom();

    private static final String FILE_NAME = "WaitingRoom.ser";

    private SerializeTool<WaitingRoom> tool = new SerializeTool<>();


    public void initialize() {
        var newRoom =tool.read(FILE_NAME);
        if(newRoom != null) {
            room = newRoom;
            room.initTransientFields();
        }
        this.datePicker.setValue(LocalDate.now());

        if(room.getPatientCount() == 0) this.nextButton.setDisable(true);
        room.addObserver(new ChangeObserver<WaitingRoom>() { // Clear selections
            @Override
            public void update(WaitingRoom observer) {
                name.clear();
                time.clear();
                datePicker.setValue(LocalDate.now());
                emergency.setSelected(false);

            }
        });
        room.addObserver(new ChangeObserver<WaitingRoom>() { // Update the displays
            @Override
            public void update(WaitingRoom observer) {
                textArea.setText(observer.toString());
                Patient undergoingTreatment = observer.getPatientUndergoingTreatment();
                if(undergoingTreatment == null) {
                    treatment.setText("");
                }else {
                    treatment.setText(undergoingTreatment.getName());
                }
                Patient inPreparation = observer.getPatientInPreparation();
                if(inPreparation == null) {
                    preparation.setText("");
                }else {
                    preparation.setText(inPreparation.getName());
                }
                int count = observer.getPatientCount();
                leftPatient.setText(String.valueOf(count));
                if(count == 0) {
                    nextButton.setDisable(true);
                }else {
                    nextButton.setDisable(false);
                }

            }
        });

        if(newRoom != null) {
            room.notifyAllObservers();
        }
    }
    @FXML
    private void onAddButtonClicked() {
        try {
            String name = this.name.getText();
            String time = this.time.getText();
            LocalDate date = this.datePicker.getValue();
            boolean isEmergency = this.emergency.isSelected();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            LocalDateTime dateTime = date.atTime(LocalTime.parse(time, formatter));
            if(dateTime.truncatedTo(ChronoUnit.MINUTES).isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) throw new Exception();
            this.room.addPatient(name, dateTime, isEmergency);
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect data entered!");
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void onCheckBoxClicked() {
        if(this.emergency.isSelected()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            this.time.setText(LocalTime.now().format(formatter));
            this.datePicker.setValue(LocalDate.now());
        }
    }

    @FXML
    private void onNextButtonClicked() {
        this.room.treatNextPatient();
    }

    @FXML
    private void onSaveButtonClicked() {
        tool.write(this.room, FILE_NAME);
    }
}
