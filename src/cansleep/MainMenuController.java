package cansleep;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javax.swing.JOptionPane;

public class MainMenuController implements Initializable {
    
    
    @FXML
    private Label minuteLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private HBox remainingTimeHBox;
    @FXML
    private JFXTextField minuteTextField;
    @FXML
    private JFXTextField secondTextField;
    @FXML
    private JFXComboBox<String> actionComboBox;
    
    static Timer timer;
    int remainingMinutes;
    int remainingSeconds;
    int secondsPassed;
    int timeSet;
    
    TrayIcon trayIcon;
    SystemTray tray;
    PopupMenu popup;
    MenuItem openMenuItem;
    MenuItem exitMenuItem;
    Image image;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        actionComboBox.getItems().addAll("shutdown","sleep");
        actionComboBox.setValue("shutdown");
        
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        URL imageUrl = System.class.getResource("/images/SystemTrayIcon.jpg");
        image = Toolkit.getDefaultToolkit().getImage(imageUrl);
        trayIcon = new TrayIcon(image);
        tray = SystemTray.getSystemTray();

        popup = new PopupMenu();

        // Create a pop-up menu components
        openMenuItem = new MenuItem("Open");
        exitMenuItem = new MenuItem("Exit and stop the timer");

        //Add components to pop-up menu
        popup.add(openMenuItem);
        popup.addSeparator();
        popup.add(exitMenuItem);

        trayIcon.setPopupMenu(popup);

        exitMenuItem.addActionListener(e -> exitMenuItemClicked());
    }
    
    private void exitMenuItemClicked() {
        System.exit(0);
        tray.remove(trayIcon);
    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        timer.cancel();
        remainingTimeHBox.setVisible(false);
    }

    @FXML
    private void setButtonClicked(ActionEvent event) {
        
        if(Integer.parseInt(secondTextField.getText()) > 59){
            JOptionPane.showMessageDialog(null, "Input for seconds can not be 60 or higher.", null, JOptionPane.ERROR_MESSAGE);
            secondTextField.setText("0");
            return;
        }
        
        timeSet = Integer.parseInt(minuteTextField.getText())*60 + Integer.parseInt(secondTextField.getText());
        timer = new Timer();
        
        if(actionComboBox.getValue().equals("shutdown")){
            remainingTimeHBox.setVisible(true);
            
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    secondsPassed++;
                    System.out.println(secondsPassed);
                    Platform.runLater(() -> {
                        remainingMinutes = (timeSet - secondsPassed) / 60;
                        remainingSeconds = (timeSet - secondsPassed) % 60;
                        minuteLabel.setText(Integer.toString(remainingMinutes));
                        secondLabel.setText(Integer.toString(remainingSeconds)); 
                    });
                    if(secondsPassed >= timeSet){
                        timer.cancel();
                        try {
                            Runtime.getRuntime().exec("shutdown -s -t 0");
                        } catch (IOException ex) {
                            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };  
            timer.scheduleAtFixedRate(task,0,1000);
        }
        
        else{
            remainingTimeHBox.setVisible(true);
            
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    secondsPassed++;
                    System.out.println(secondsPassed);
                    Platform.runLater(() -> {
                        remainingMinutes = (timeSet - secondsPassed) / 60;
                        remainingSeconds = (timeSet - secondsPassed) % 60;
                        minuteLabel.setText(Integer.toString(remainingMinutes));
                        secondLabel.setText(Integer.toString(remainingSeconds)); 
                    });
                    if(secondsPassed >= timeSet){
                        timer.cancel();
                        try {
                            Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
                        } catch (IOException ex) {
                            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task,0,1000);
            
        }
    }
    
    
}
