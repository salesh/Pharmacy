package application.controlers;

import java.io.IOException;

import application.PharmacyMessage;
import application.ValidationInput;
import application.MySQLOperations;
import application.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button btnLog;
    
    @FXML
    private TextField fieldUser;
    
    @FXML
    private PasswordField fieldPass;
       
    private MySQLOperations mysqlOp = null;
        
    private Stage myStage;
    	
    public MainController(){
    	mysqlOp = new MySQLOperations();
    }
    
    @FXML
    void authenticationLogIn() throws PharmacyMessage {
    	String strUser = fieldUser.getText();
    	String strPass = fieldPass.getText();
    	User user = new User();
    	try{
    		if(ValidationInput.validationUsername(strUser) && ValidationInput.validationPassword(strPass)){
    			user = mysqlOp.findUser(strUser);
    			if(user == null){
					throw new PharmacyMessage("Uneli ste nepostojećeg korisnika." , AlertType.ERROR, "Greška");
	    		}
	        	if(user.getHashPass().equals(ValidationInput.getSHA256(strPass))){
	        		setPharmacyStage(user);
	        	}else{
					throw new PharmacyMessage("Pogrešna lozinka.", AlertType.ERROR, "Greška");
	        	}
    		}else{
				throw new PharmacyMessage("Proverite lozinku i korisničko ime.", AlertType.ERROR, "Greška");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
    		throw new PharmacyMessage("Greška u programu", AlertType.ERROR, "Greška");
		} 
    }
    
    public void setPharmacyStage(User user){
		try {
	    	FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/fxml_pharmacy.fxml"));
	        Parent root = (Parent) myLoader.load();
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("Apoteka");
	        stage.setScene(scene);
			stage.resizableProperty().setValue(Boolean.FALSE);
	        myStage.close();
	        stage.show();
		
			PharmacyController fxmlCont = (PharmacyController) myLoader.getController();
			fxmlCont.setPrevStage(myStage);
			fxmlCont.setMyStage(stage);
			fxmlCont.setLabelUser(user.getUsername());
			fxmlCont.setAdminOptionsVisible(user.isTip());
			fxmlCont.setUser(user);
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
   
    public void setMyStage(Stage stage){
        this.myStage = stage;
    }
}
