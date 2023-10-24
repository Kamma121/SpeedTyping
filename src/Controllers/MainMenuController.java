package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Label messageLabel;
    @FXML
    private RadioButton radioButtonWords;
    @FXML
    private RadioButton radioButtonLetters;
    private ToggleGroup toggleGroup;

    public void initialize() {
        toggleGroup = new ToggleGroup();
        radioButtonLetters.setToggleGroup(toggleGroup);
        radioButtonWords.setToggleGroup(toggleGroup);
    }

    public void switchToAppScene(ActionEvent event) throws IOException {
        if (toggleGroup.getSelectedToggle() == null) {
            messageLabel.setText("Please select a mode.");
            return;
        }
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        String mode = selectedRadioButton.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AppScene.fxml"));
        Parent root = loader.load();
        AppSceneController appSceneController = loader.getController();
        appSceneController.initialize(mode);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}