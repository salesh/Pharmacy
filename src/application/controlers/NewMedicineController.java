package application.controlers;

import javafx.stage.Stage;

import java.sql.Timestamp;

import application.MySQLOperations;
import application.PharmacyMessage;
import application.ValidationInput;
import application.entity.Medicines;
import application.entity.OrderPlaced;
import application.entity.Supplier;
import application.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewMedicineController {
    @FXML
    private TextField fieldIdMed;

    @FXML
    private TextField fieldNameManuf;

    @FXML
    private TextField fieldSup;

    @FXML
    private TextField fieldJM;

    @FXML
    private TextField fieldCap;

    @FXML
    private TextField fieldNameMed;

    @FXML
    private Button btnCreateMed;
    
    @FXML
    private Button btnCancelNewMed;
    
    private MySQLOperations mysqlOp;
    
    private User user;
    
    private Stage myStage;
	
    private Stage prevStage;
    
    public NewMedicineController() {
    	mysqlOp = new MySQLOperations();
	}

    @FXML
    void createMed() throws PharmacyMessage {
    	String strNameMed = fieldNameMed.getText();
    	String strCap = fieldCap.getText();
    	String strJM = fieldJM.getText();
    	String strNameSup = fieldSup.getText();
    	String strNameManuf = fieldNameManuf.getText();
    	String strIdMed = fieldIdMed.getText();
    	Medicines med = null;
    	
    	validationInput(strCap, strJM, strIdMed);
	
    	try {
    		med = mysqlOp.findMedicine(strIdMed);
	    	if(med != null){
	    		throw new PharmacyMessage("Lek sa tom šifrom je već u upotrebi", AlertType.ERROR, "Greška");
	    	} else{
	    		med = new Medicines();
	    		med.setCodeMedicine(Integer.parseInt(strIdMed));
	    		med.setName(strNameMed);
	    		med.setNameManufacture(strNameManuf);
	    		med.setUnitMeasure(strJM);
	    		med.setQuantity(Double.parseDouble(strCap));
	    		mysqlOp.createMedicine(med);
	    		med = mysqlOp.findMedicine(strIdMed);	  
	    		
	    		Supplier sup = mysqlOp.findSupplier(strNameSup);
	    		if(sup == null && !(strNameSup.length() > 0)){
	    			sup = new Supplier();
		    		sup.setIdSupplier(1);
	    		} else {
	    			sup = new Supplier();
		    		sup.setName(strNameSup);
		    		mysqlOp.createSupplier(sup);
		    		sup = mysqlOp.findSupplier(strNameSup);
	    		}
	    		
	    		OrderPlaced op = new OrderPlaced();
	    		op.setDateOrdering(new Timestamp(System.currentTimeMillis()));
	    		op.setIdMedicines(med.getIdMedicine());
	    		op.setIdPerson(user.getIdPerson());
	    		op.setIdSupplier(sup.getIdSupplier());
	    		op.setQuantity(med.getQuantity());
	    		mysqlOp.createOrder(op);

	    		myStage.close();
	    		prevStage.show();
	    		throw new PharmacyMessage("Uspešno kreirana nabavka za novi lek", AlertType.INFORMATION, "Info");
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

	private void validationInput(String strCap, String strJM, String strIdMen) throws PharmacyMessage {
    	if((!(strCap.length() > 0) || !ValidationInput.validationNumber(strCap)) || !(strJM.length() > 0) || (!(strIdMen.length() > 0) && !ValidationInput.validationNumber(strIdMen))){
    		throw new PharmacyMessage("Proverite unos kod obaveznih polja", AlertType.ERROR, "Greška");
    	}		
	}
	
	public void setPrevStage(Stage prevStage) {
		this.prevStage = prevStage;
	}

	public void setMyStage(Stage myStage) {
		this.myStage = myStage;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
