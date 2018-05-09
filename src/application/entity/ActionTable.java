package application.entity;
import javafx.beans.property.SimpleStringProperty;

public class ActionTable {
		private final StatePharmacyTable stateMedicine;
		private final SimpleStringProperty actionName;
		private final SimpleStringProperty actionCap;
		private final SimpleStringProperty actionOrderName;
		private final SimpleStringProperty actionCode;
		private Supplier sup;
		
		public ActionTable(StatePharmacyTable stateMedicine, String sName, String sCap, String sOrderName, String sCode){
			this.stateMedicine = stateMedicine;
			this.actionName = new SimpleStringProperty(sName);
			this.actionCap = new SimpleStringProperty(sCap);
			this.actionOrderName = new SimpleStringProperty(sOrderName);
			this.actionCode = new SimpleStringProperty(sCode);
		}
		
		public SimpleStringProperty actionNameProperty(){
			return actionName;
		}
		
		public void setActionNameProperty(String sON){
			this.actionName.set(sON);
		}
		
		public SimpleStringProperty actionCapProperty(){
			return actionCap;
		}
		
		public void setActionCapProperty(String sCap){
			this.actionCap.set(sCap);
		}
		
		public SimpleStringProperty actionOrderNameProperty(){
			return actionOrderName;
		}
		
		public void setActionOrderNameProperty(String sOrderName){
			this.actionOrderName.set(sOrderName);
		}
		
		public SimpleStringProperty actionCodeProperty(){
			return actionCode;
		}
		
		public void setActionCodeProperty(String sCode){
			this.actionCode.set(sCode);
		}

		public StatePharmacyTable getStateMedicine() {
			return stateMedicine;
		}

		public Supplier getSup() {
			return sup;
		}

		public void setSup(Supplier sup) {
			this.sup = sup;
		}
}
