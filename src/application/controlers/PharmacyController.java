package application.controlers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.MySQLOperations;
import application.PharmacyMessage;
import application.ValidationInput;
import application.entity.ActionTable;
import application.entity.ReportsTable;
import application.entity.StatePharmacyTable;
import application.entity.Supplier;
import application.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;


public class PharmacyController implements Initializable{

	@FXML
	private TableView<ReportsTable> reportsTView;
	
	@FXML
	private TableColumn<ReportsTable, String> colReportName;

	@FXML
	private TableColumn<ReportsTable, Double> colReportCap;
	
    @FXML
    private TableColumn<ReportsTable, String> colReportDate;
    
    @FXML
    private TableColumn<ReportsTable, String> colReportManuf;
    
    @FXML
    private TableColumn<ReportsTable, String> colReportSupplier;
    
    @FXML
    private TableColumn<ReportsTable, String> colReportCode;
        
	@FXML
    private Label labelUser;
    
    @FXML
    private Button btnLogOut;
   
    @FXML
    private Tab tabOrder;
    
    @FXML
    private Button btnSearch;

    @FXML
    private RadioButton rbReportIssuing;
    
    @FXML
    private RadioButton rbReportOrder;
    
    @FXML
    private ToggleGroup tipGroup;

    @FXML
    private DatePicker dateTo;

    @FXML
    private TextField inputSearchReports;

    @FXML
    private DatePicker dateFrom;
    
    @FXML
    private TableView<ActionTable> actionTableV;
    
    @FXML 
    private TableColumn<ActionTable, String> colActionCap;

    @FXML 
    private TableColumn<ActionTable, String> colActionCode;
    
    @FXML
    private TableColumn<ActionTable, String> colActionName;
    
    @FXML
    private TableColumn<ActionTable, String> colActionOrderName;
    
    @FXML
    private TableView<StatePharmacyTable> stateTableV;
    
    @FXML
    private TableColumn<StatePharmacyTable, Double> colStateCap;

    @FXML
    private TableColumn<StatePharmacyTable, String> colStateCode;
    
    @FXML
    private TableColumn<StatePharmacyTable, String> colStateManuf;

    @FXML
    private TableColumn<StatePharmacyTable, String> colStateName;

    @FXML
    private TableColumn<StatePharmacyTable, String> colStateJM;
    
    @FXML
    private TableColumn<StatePharmacyTable, Boolean> colStateActionBox;
            
    @FXML
    private TextField inputNameSearch;

    @FXML
    private Button btnOrderRec;

    @FXML
    private Button btnSearchState;

    @FXML
    private Button btnOrderClear;
    
    @FXML
    private Button btnAddToActionOrder;

    @FXML
    private Button btnAddToActionIssuing;
    
    @FXML
    private Button btnDeactive;
    
    @FXML
    private Button btnActionExecute;
    
    @FXML
    private Button btnActionClear;
    
    @FXML
    private Button btnNewAcc;
        
    @FXML
    private Button btnNewMed;
    
    private Boolean isModeIssuing; 
    
    private MySQLOperations mysqlOp;
    
    private User user;

    private Stage prevStage;
    
    private Stage myStage;
    
    private Parent root;

    final Map<Integer, StatePharmacyTable> mapSearchState;
    
    final ObservableList<ReportsTable> dataReportsT;

    final ObservableList<ActionTable> dataActionT;
   
    final ObservableList<StatePharmacyTable> dataStateT;

    public PharmacyController() {
    	dataReportsT = FXCollections.observableArrayList();
    	dataActionT = FXCollections.observableArrayList();
    	dataStateT = FXCollections.observableArrayList();
		mysqlOp = new MySQLOperations();
		mapSearchState = new HashMap<>();
	}
   
    @FXML
    void fnSearchStateOfPharmacy() throws PharmacyMessage {
    	Boolean isEmptySearch =  !(inputNameSearch.getText().length() > 0);
    	buildSqlAndCallSelectState(isEmptySearch,inputNameSearch.getText());
    	if(dataStateT.size() > 0){
    		btnDeactive.setDisable(false);
    		btnAddToActionOrder.setDisable(false);
    		btnAddToActionIssuing.setDisable(false);
    	}
    }
    
    private void buildSqlAndCallSelectState(Boolean isEmptySearch, String strSearch) throws PharmacyMessage {
    	String sqlSearch = " select naziv, kolicina, sifra_leka, jedinica_mere, proizvodjac, key_lek from apoteka.stanje_lekova";
    	if(!isEmptySearch){
   		 	sqlSearch += " where naziv like ? or sifra_leka like ? ";
    	}
    	dataStateT.clear();
    	try {
			mysqlOp.searchState(strSearch, sqlSearch, dataStateT, stateTableV);
		} catch (Exception e) {
			e.printStackTrace();
	    	throw new PharmacyMessage("Greška pri radu sa bazom", AlertType.ERROR, "Greška");
		}
	}

	@FXML
    void fnSearchReports() throws PharmacyMessage {
    	
    	Boolean isEmptySearch =  !(inputSearchReports.getText().length() > 0);
    	Boolean isDateFrom = dateFrom.getValue() != null ? true : false;
    	Boolean isDateTo = dateTo.getValue() != null ? true : false;
    	ValidationInput.validateSearchReports(tipGroup, isDateFrom, isDateTo);
    	buildSqlAndCallSelectReport(isEmptySearch, isDateFrom, isDateTo);    	
    }
    
    private void buildSqlAndCallSelectReport(Boolean isEmptySearch, Boolean isDateFrom, Boolean isDateTo) throws PharmacyMessage{
    	String sqlSearch = "";
    	long dF = 0L;
    	long dT = 0L;
    	int mode = 0; 
    	String rClicked = (tipGroup.getSelectedToggle().getUserData().toString());
    	String strSearch = inputSearchReports.getText();

    	if(rClicked.equals(rbReportIssuing.getUserData())){
    		sqlSearch += " select l.naziv, il.kolicina, il.datum_izdavanja, l.proizvodjac, db.naziv, l.sifra_leka ";
        	sqlSearch += " from apoteka.stanje_lekova l join apoteka.izdavanje_leka il on l.key_lek = il.key_lek ";
        	sqlSearch += " left join apoteka.narucivanje_leka nl on l.key_lek = nl.key_lek ";
        	sqlSearch += " left join apoteka.dobavljac db on db.key_dobavljac = nl.key_dobavljac ";

    	}else {
    		sqlSearch += " select l.naziv, nl.kolicina, nl.datum_narucivanja, l.proizvodjac, db.naziv, l.sifra_leka ";
        	sqlSearch += " from apoteka.stanje_lekova l join apoteka.narucivanje_leka nl on l.key_lek = nl.key_lek ";
        	sqlSearch += " join apoteka.dobavljac db on db.key_dobavljac = nl.key_dobavljac ";
    	}
    	
    	if(isDateFrom && isDateTo){
        	dF = Date.valueOf(dateFrom.getValue()).getTime();
        	dT = Date.valueOf(dateTo.getValue()).getTime();
        	if(rClicked.equals(rbReportIssuing.getUserData())){
        	    sqlSearch += " where il.datum_izdavanja >= ? and il.datum_izdavanja <= ? ";
        	    mode = 1;
        	} else {
        	    sqlSearch += " where nl.datum_narucivanja >= ? and nl.datum_narucivanja <= ? ";
        	    mode = 1;
        	}
    	}
    	if(mode == 0 && !isEmptySearch){
    		 sqlSearch += " where l.naziv like ? or l.sifra_leka like ? ";
    	}else if(!isEmptySearch){
   		 	sqlSearch += " and l.naziv like ? or l.sifra_leka like ? ";
    	}
    	dataReportsT.clear();
    	try {
			mysqlOp.searchReports(sqlSearch, dF,dT,strSearch, mode, dataReportsT, reportsTView);
		} catch (Exception e) {
			e.printStackTrace();
	    	throw new PharmacyMessage("Greška pri radu sa bazom", AlertType.ERROR, "Greška");
		}
	}
    
    @FXML
    void fnAddToActionOrder() {
    	colActionOrderName.setVisible(true);
    	isModeIssuing = false;
    	fnAddToAction();
    }
    
    @FXML 
    void fnAddToActionIssuing(){
    	isModeIssuing = true;
    	fnAddToAction();
    }
    
    @FXML 
    void fnDeactive() throws PharmacyMessage{
    	List<Integer> listDeleteKeys = new ArrayList<>();
    	for(StatePharmacyTable spt : dataStateT){
    		if(spt.isAction()){
    			listDeleteKeys.add(spt.getIdMedicine());
    		}
    	}
    	try {
			mysqlOp.deactiveMed(listDeleteKeys);
		} catch (Exception e) {
			e.printStackTrace();
	    	throw new PharmacyMessage("Greška pri radu sa bazom", AlertType.ERROR, "Greška");
		}
    	fnAfterSucces();
    	throw new PharmacyMessage("Uspešno obrisani lekovi", AlertType.INFORMATION, "Brisanje");
    }
    
    void fnAddToAction(){
    	for(StatePharmacyTable spt : dataStateT){
    		if(spt.isAction()){
    			ActionTable at = new ActionTable(spt, spt.getStateName(), "", "", spt.stateCodeMProperty().getValue());
    			dataActionT.add(at);
    		}
    	}
    	actionTableV.setItems(dataActionT);
    	if(!dataActionT.isEmpty()){
    		btnAddToActionIssuing.setDisable(true);
    		btnAddToActionOrder.setDisable(true);
    		btnDeactive.setDisable(true);
    		btnSearchState.setDisable(true);
    		btnActionClear.setDisable(false);
    		btnActionExecute.setDisable(false);
    	}
    }
    
    @FXML
    void fnActionExecute() throws PharmacyMessage{
    	try{
        	List<ActionTable> listMedicine = new ArrayList<>();
	    	if(isModeIssuing){
	    		for(ActionTable at : dataActionT){
	    			if(at.actionCapProperty() != null && at.actionCapProperty().getValue().length() > 0){
	    				if(!(Double.valueOf(at.actionCapProperty().getValue()) <= at.getStateMedicine().getStateCap())){
		    				throw new PharmacyMessage("Može se izdati samo " + at.getStateMedicine().getStateCap() + " za " + at.actionNameProperty().getValue(), AlertType.ERROR, "Greška");
	    				}
	    				listMedicine.add(at);
	    			}else {
	    				throw new PharmacyMessage("Niste uneli količinu za " + at.actionNameProperty().getValue(), AlertType.ERROR, "Greška");
	    			}
	    		}
				mysqlOp.createIssuingForMed(listMedicine, user.getIdPerson());
				mysqlOp.updateMedAction(listMedicine, isModeIssuing);
				fnAfterSucces();
				throw new PharmacyMessage("Uspešno izdavanje ", AlertType.INFORMATION, "Info");
	    	}else{
	    		for(ActionTable at : dataActionT){
					Supplier sup = new Supplier();
	    			if(at.actionCapProperty() != null && at.actionCapProperty().getValue().length() > 0){
	    				if(at.actionOrderNameProperty() != null && at.actionOrderNameProperty().getValue().length() > 0){
	    					sup = mysqlOp.findSupplier(at.actionOrderNameProperty().getValue());
	    					if(sup == null){
	    						sup = new Supplier();
	    						sup.setName(at.actionOrderNameProperty().getValue());
	    						mysqlOp.createSupplier(sup);
	    						sup = mysqlOp.findSupplier(at.actionOrderNameProperty().getValue());
	    					}
	    				} else {
	    					sup = new Supplier();
	    					sup.setIdSupplier(1);
	    				}
	    				at.setSup(sup);
	    				listMedicine.add(at);
	    			}else {
	    				throw new PharmacyMessage("Niste uneli količinu za " + at.actionNameProperty(), AlertType.ERROR, "Greška");
	    			}
	    		}
				mysqlOp.createOrderForMed(listMedicine, user.getIdPerson());
				mysqlOp.updateMedAction(listMedicine, isModeIssuing);
				fnAfterSucces();
				throw new PharmacyMessage("Uspešna nabavka ", AlertType.INFORMATION, "Info");
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
			throw new PharmacyMessage("Neispravan unos", AlertType.INFORMATION, "Info");
    	}
    }
    
    @FXML
    void fnActionClear(){
    	colActionOrderName.setVisible(false);
    	dataActionT.clear();
    	btnSearchState.setDisable(false);
    }
    
    @FXML
    void fnLogOut() {
    	myStage.close();
	    prevStage.show();	
    }
        
    void fnAfterSucces() throws PharmacyMessage{
    	dataStateT.clear();
    	dataActionT.clear();
    	btnAddToActionIssuing.setDisable(true);
    	btnAddToActionOrder.setDisable(true);
    	btnDeactive.setDisable(true);
    	fnSearchStateOfPharmacy();
    }
    
    @FXML
    void setNewAccountStage(){
		try {
	    	FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/fxml_newUser.fxml"));
	        Parent root = (Parent) myLoader.load();
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("Novi nalog");
	        stage.setScene(scene);
			stage.resizableProperty().setValue(Boolean.FALSE);
	        myStage.close();
	        stage.show();
		
			NewAccountController fxmlCont = (NewAccountController) myLoader.getController();
			fxmlCont.setPrevStage(myStage);
			fxmlCont.setMyStage(stage);
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void setNewMedicineStage(){
		try {
	    	FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/fxml_newMed.fxml"));
	        root = (Parent) myLoader.load();
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("Novi lek");
	        stage.setScene(scene);
			stage.resizableProperty().setValue(Boolean.FALSE);
	        myStage.close();
	        stage.show();
		
			NewMedicineController fxmlCont = (NewMedicineController) myLoader.getController();
			fxmlCont.setPrevStage(myStage);
			fxmlCont.setMyStage(stage);
			fxmlCont.setUser(user);
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
      
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Reports 
		colReportName.setCellValueFactory(new PropertyValueFactory<ReportsTable, String>("rName"));
    	colReportCap.setCellValueFactory(new PropertyValueFactory<ReportsTable, Double>("rCap"));
    	colReportDate.setCellValueFactory(new PropertyValueFactory<ReportsTable, String>("rDate"));
    	colReportManuf.setCellValueFactory(new PropertyValueFactory<ReportsTable, String>("rNameManuf"));
    	colReportSupplier.setCellValueFactory(new PropertyValueFactory<ReportsTable, String>("rNameSup"));
    	colReportCode.setCellValueFactory(new PropertyValueFactory<ReportsTable, String>("rCodeM"));	
    	rbReportIssuing.setUserData("rbIssuing");
       	rbReportOrder.setUserData("rbOrder");
    	reportsTView.setPlaceholder(new Label("Nije odabrano"));

    	//State
    	colStateName.setCellValueFactory(new PropertyValueFactory<StatePharmacyTable, String>("stateName"));
    	colStateCap.setCellValueFactory(new PropertyValueFactory<StatePharmacyTable, Double>("stateCap"));
    	colStateJM.setCellValueFactory(new PropertyValueFactory<StatePharmacyTable, String>("stateJM"));
    	colStateManuf.setCellValueFactory(new PropertyValueFactory<StatePharmacyTable, String>("stateManuf"));
    	colStateCode.setCellValueFactory(new PropertyValueFactory<StatePharmacyTable, String>("stateCodeM"));
    	colStateActionBox.setCellValueFactory(new PropertyValueFactory<>("action"));
    	colStateActionBox.setCellFactory(CheckBoxTableCell.forTableColumn(colStateActionBox));
    	colStateActionBox.setEditable(true); 	
    	stateTableV.setEditable(true);
    	stateTableV.setPlaceholder(new Label("Nije odabrano"));
    	    
    	//Action
    	colActionName.setCellValueFactory(new PropertyValueFactory<ActionTable, String>("actionName"));
    	colActionCode.setCellValueFactory(new PropertyValueFactory<ActionTable, String>("actionCode"));
    	colActionCap.setCellFactory(TextFieldTableCell.forTableColumn());
    	colActionCap.setOnEditCommit(
    	    new EventHandler<CellEditEvent<ActionTable, String>>() {
    	        @Override
    	        public void handle(CellEditEvent<ActionTable, String> t) {
    	            ((ActionTable) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setActionCapProperty(t.getNewValue());
    	        }
    	    }
    	);
    	colActionOrderName.setCellFactory(TextFieldTableCell.forTableColumn());
    	colActionOrderName.setOnEditCommit(
        	    new EventHandler<CellEditEvent<ActionTable, String>>() {
        	        @Override
        	        public void handle(CellEditEvent<ActionTable, String> t) {
        	            ((ActionTable) t.getTableView().getItems().get(
        	                t.getTablePosition().getRow())
        	                ).setActionOrderNameProperty(t.getNewValue());
        	        }
        	    }
        	);
    	actionTableV.setEditable(true);
    	actionTableV.setPlaceholder(new Label("Nije odabrano"));
    	    	
	}
	
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

	public void setLabelUser(String username) {
		this.labelUser.setText(username);
	}
    
	public void setUser(User user) {
		this.user = user;
	}
		
	public void setAdminOptionsVisible(Boolean isAdmin){
		this.tabOrder.setDisable(!isAdmin);
		this.btnAddToActionOrder.setVisible(isAdmin);
		this.btnDeactive.setVisible(isAdmin);
	}

	public void setMyStage(Stage stage) {
		 this.myStage = stage;
	}

	public Boolean getIsModeIssuing() {
		return isModeIssuing;
	}

	public void setIsModeIssuing(Boolean isModeIssuing) {
		this.isModeIssuing = isModeIssuing;
	}

    
}
