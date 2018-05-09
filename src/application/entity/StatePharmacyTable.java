package application.entity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StatePharmacyTable {
		private final Integer idMedicine;
		private final SimpleStringProperty stateName;
		private final SimpleDoubleProperty stateCap;
		private final SimpleStringProperty stateJM;
		private final SimpleStringProperty stateManuf;
		private final SimpleStringProperty stateCodeM;
	    private final BooleanProperty action;

		public StatePharmacyTable(String sName, Double sCap, String sCodeM,String sJM, String sManuf, Integer idMedicine){
			this.action = new SimpleBooleanProperty(false);
			this.stateCodeM = new SimpleStringProperty(sCodeM);
			this.stateName = new SimpleStringProperty(sName);
			this.stateCap = new SimpleDoubleProperty(sCap);
			this.stateJM = new SimpleStringProperty(sJM);
			this.stateManuf = new SimpleStringProperty(sManuf);
			this.idMedicine = idMedicine;

		}

		public String getStateName() {
			return stateName.get();
		}

		public void setStateName(String sName) {
			stateName.set(sName);
		}

		public Double getStateCap() {
			return stateCap.get();
		}

		public void setStateCap(Double sCap) {
			stateCap.set(sCap);
		}
		
		public String getStateJM() {
			return stateJM.get();
		}

		public void setStateJM(String sJM) {
			stateJM.set(sJM);
		}
		
		public String getStateManuf() {
			return stateManuf.get();
		}

		public void setStateManuf(String sManuf) {
			stateManuf.set(sManuf);
		}
		
		public SimpleStringProperty stateNameProperty(){
			return stateName;
		}
		public void setStateNameProperty(String sName){
			this.stateName.set(sName);
		}
		
		public SimpleDoubleProperty stateCapProperty(){
			return stateCap;
		}
		
		public void setStateCapProperty(Double sCap){
			this.stateCap.set(sCap);
		}
		
		public SimpleStringProperty stateJMProperty(){
			return stateJM;
		}
		
		public void setStateJMProperty(String sJM){
			this.stateJM.set(sJM);
		}
		
		public SimpleStringProperty stateManufProperty(){
			return stateManuf;
		}
		
		public void setStateManufProperty(String sMan){
			this.stateManuf.set(sMan);
		}
	
		public SimpleStringProperty stateCodeMProperty(){
			return stateCodeM;
		}
		
		public void setStateCodeMProperty(String sCodeM){
			this.stateCodeM.set(sCodeM);
		}
		
		public boolean isAction() {
	        return action.get();
	    }

	    public void setAction(boolean selected) {
	       this.action.set(selected);
	    }
	    
	    public BooleanProperty actionProperty() {
	        return action;
	    }
    
		public Integer getIdMedicine() {
			return idMedicine;
		}
		
}
