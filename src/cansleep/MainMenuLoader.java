package cansleep;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainMenuLoader extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        
        MainMenuController controller = createControllerObject();
        
        stage.setTitle("Cansleep");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> closeRequest(controller.tray, controller.trayIcon));
        controller.openMenuItem.addActionListener(e -> openMenuItemClicked(stage, controller.tray, controller.trayIcon));
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void openMenuItemClicked(Stage stage, SystemTray tray, TrayIcon trayIcon) {
        Platform.runLater(() -> stage.show());
        tray.remove(trayIcon);
    }

    private void closeRequest(SystemTray tray, TrayIcon trayIcon) {
        try {
            tray.add(trayIcon);
        } 
        catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
    }
    
    private MainMenuController createControllerObject(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        try {
            Parent rootLayout = (Parent)loader.load();
        } 
        catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (MainMenuController)loader.getController();
    }
    
}
