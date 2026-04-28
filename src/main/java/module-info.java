module at.htlleonding.medical {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.htlleonding.medical to javafx.fxml;
    opens at.htlleonding.medical.controller to javafx.fxml;

    exports at.htlleonding.medical;
    exports at.htlleonding.medical.controller;
}