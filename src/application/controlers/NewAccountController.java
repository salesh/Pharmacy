package application.controlers;

import application.MySQLOperations;
import application.PharmacyMessage;
import application.ValidationInput;
import application.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewAccountController {


    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldJMBG;

    @FXML
    private TextField fieldSurname;

    @FXML
    private TextField fieldUser;

    @FXML
    private PasswordField fieldPass;
    
    @FXML
    private Button btnCreateAcc;
    
    @FXML
    private Button btnCancelNewAcc;
    
    @FXML
    private CheckBox checkAdmin;
    
    private MySQLOperations mysqlOp;
    
    private Stage prevStage;

    private Stage myStage;
	
    public NewAccountController() {
    	mysqlOp = new MySQLOperations();
	}
    
    @FXML
    void createAccount() throws PharmacyMessage{
    	
    	String strUser = fieldUser.getText();
    	String strPass = fieldPass.getText();
    	String strName = fieldName.getText();
    	String strSurname= fieldSurname.getText();
    	String strJMBG = fieldJMBG.getText();
    	Boolean boolAdmin = checkAdmin.isSelected();
    	User user = null;
    	
    	validationInput(strUser, strPass, strName, strSurname, strJMBG);
	
    	try {
			user = mysqlOp.findUser(strUser);
	    	if(user != null){
	    		throw new PharmacyMessage("Korisničko ime već u upotrebi", AlertType.ERROR, "Greška");
	    	} else{
	    		user = new User();
	    		user.setUsername(strUser);
	    		user.setHashPass(ValidationInput.getSHA256(strPass));
	    		user.setName(strName);
	    		user.setSurname(strSurname);
	    		user.setJmbg(strJMBG);
	    		user.setTip(boolAdmin);
	    		mysqlOp.createUser(user);
	    		
	    		myStage.close();
	    		prevStage.show();
	    		throw new PharmacyMessage("Uspešno kreiran nalog", AlertType.INFORMATION, "Info");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
    		throw new PharmacyMessage("Greška u programu", AlertType.ERROR, "Greška");
		}
    	
    }
    
    @FXML 
    void fnCancelReq(){
		myStage.close();
		prevStage.show();
    }
    
	private void validationInput(String strUser, String strPass, String strName, String strSurname, String strJMBG) throws PharmacyMessage {
    	if(!ValidationInput.validationUsername(strUser)){
    		throw new PharmacyMessage("Niste ispunili kriterijume korisničkog imena", AlertType.ERROR, "Greška");
    	}
    	
    	if(!ValidationInput.validationPassword(strPass)){
    		throw new PharmacyMessage("Niste ispunili kriterijume korisničke lozinke", AlertType.ERROR, "Greška");
    	}
    	
    	if(!ValidationInput.validationNameSurname(strName)){
    		throw new PharmacyMessage("Ime može da sadrži samo slova", AlertType.ERROR, "Greška");
    	}
    	
    	if(!ValidationInput.validationNameSurname(strSurname)){
    		throw new PharmacyMessage("Prezime može da sadrži samo slova", AlertType.ERROR, "Greška");
    	}
    	
    	if(strJMBG.length() > 0 && !ValidationInput.validationJMBG(strJMBG)){
    		throw new PharmacyMessage("JMBG mora da ima 13 brojeva", AlertType.ERROR, "Greška");
    	}		
	}

	public void setPrevStage(Stage stage) {
		this.prevStage = stage;
	}

	public void setMyStage(Stage stage) {
		 this.myStage = stage;
	}
}
