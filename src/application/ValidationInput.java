package application;

import java.security.MessageDigest;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class ValidationInput {
	public static boolean validationUsername(String logUser) {
    	String pattern = "^[a-z0-9_-]{3,15}$";
    	return logUser.matches(pattern);
	}
    
	public static Boolean validationPassword(String logPass) {
    	String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    	return logPass.matches(pattern);
    }
	
	public static boolean validationNameSurname(String logStr) {
    	String pattern = "^[a-zA-Z ]*$";
    	return logStr.matches(pattern);
	}
    
	public static boolean validationJMBG(String logJMBG) {
    	String pattern = "^[0-9]{13}$";
    	return logJMBG.matches(pattern);
	}
	
	public static boolean validationNumber(String logNumber) {
    	String pattern = "^[0-9]+$";
    	return logNumber.matches(pattern);
	}
	
	public static void validateSearchReports(ToggleGroup tipGroup, Boolean isDateFrom, Boolean isDateTo) throws PharmacyMessage {
    	if(tipGroup.getSelectedToggle() == null){
    		throw new PharmacyMessage("Morate izabrati bar jedan tip", AlertType.ERROR, "Greška");
    	}
    	if((isDateFrom && !isDateTo) || (!isDateFrom && isDateTo)){
    		throw new PharmacyMessage("Morate izabrati oba datuma za period", AlertType.ERROR, "Greška");
    	}		
	}
	
	public static String getSHA256(String strPass){
	    StringBuffer sb = new StringBuffer();
	    try{
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(strPass.getBytes());
	        byte byteData[] = md.digest();

	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	    } catch(Exception e){
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
}
