package Challenges;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Ch2Ls6Controller implements Initializable {
    
    @FXML
    private Label label;
    @FXML 
    private Text actiontarget;
    
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
    	int ans = (int) Math.ceil(Math.random() * 100);
    	
    	if(ans > 0 && ans <= 20 )
    		actiontarget.setText("Very doubtful");
    	else if(ans > 20 && ans <= 40)
    		actiontarget.setText("Without a doubt");
    	else if(ans > 40 && ans <= 60)
    		actiontarget.setText("Ask again later");
    	else if(ans > 60 && ans <= 80)
    		actiontarget.setText("Very doubtful");
    	else
    		actiontarget.setText("It is certain");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}