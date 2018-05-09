package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public class PharmacyMessage extends Throwable{
    public PharmacyMessage (String errMsg, AlertType typeAlert, String title){
		Alert alert = new Alert(typeAlert);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(errMsg);
		alert.showAndWait();
    }
}
