package application.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportsTable {

	private final SimpleStringProperty rName;
	private final SimpleDoubleProperty rCap;
	private final SimpleStringProperty rDate;
	private final SimpleStringProperty rNameManuf;
	private final SimpleStringProperty rNameSup;
	private final SimpleStringProperty rCodeM;

	public ReportsTable(String sName, Double sCap, String sDate, String sNameManuf, String sNameSup, String sCodeM){
		this.rName = new SimpleStringProperty(sName);
		this.rCodeM = new SimpleStringProperty(sCodeM);
		this.rCap = new SimpleDoubleProperty(sCap);
		this.rDate = new SimpleStringProperty(sDate);
		this.rNameManuf = new SimpleStringProperty(sNameManuf);
		this.rNameSup = new SimpleStringProperty(sNameSup);

	}

	public String getRName() {
		return rName.get();
	}

	public void setRName(String sName) {
		rName.set(sName);
	}

	public Double getRCap() {
		return rCap.get();
	}

	public void setRCap(Double sCap) {
		rCap.set(sCap);
	}
	
	public String getRDate() {
		return rDate.get();
	}

	public void setRDate(String sDate) {
		rDate.set(sDate);
	}
	
	public StringProperty rNameProperty(){
	    return rName;
	}
	
	public void setRNameProperty(String sName){
		this.rName.set(sName);
	}
	
	public DoubleProperty rCapProperty(){
	    return rCap;
	}
	
	public void setRCapProperty(Double sCap){
		this.rCap.set(sCap);
	}
	
	public StringProperty rDateProperty(){
	    return rDate;
	}
	
	public void setRDateProperty(String sDate){
		this.rDate.set(sDate);
	}
	
	public StringProperty rNameManufProperty(){
	    return rNameManuf;
	}
	
	public void setRNameManufProperty(String sNameManuf){
		this.rNameManuf.set(sNameManuf);
	}
	
	public StringProperty rNameSupProperty(){
	    return rNameSup;
	}
	
	public void setRNameSupProperty(String sNameSup){
		this.rNameSup.set(sNameSup);
	}
	
	public SimpleStringProperty rCodeMProperty(){
		return rCodeM;
	}
	
	public void setRCodeMProperty(String sCodeM){
		this.rCodeM.set(sCodeM);
	}
}
