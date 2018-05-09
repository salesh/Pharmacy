package application;
	
import application.controlers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/fxml_login.fxml"));
			Parent root = (Parent) myLoader.load();
			Scene scene = new Scene(root,400,400);
			stage.setTitle("Apoteka");
			stage.setScene(scene);
			stage.resizableProperty().setValue(Boolean.FALSE);
			stage.show();
			
			MainController fxmlCont = (MainController) myLoader.getController();
			fxmlCont.setMyStage(stage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
