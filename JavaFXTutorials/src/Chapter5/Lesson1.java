package Chapter5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lesson1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    	BorderPane root = new BorderPane();
    	Text title = new Text("California");
    	title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
    	title.setFill(Color.FIREBRICK);
    	
    	Image caPic = new Image("Chapter5/Desert-to-sea logo.gif");
    	ImageView ivPic = new ImageView(caPic);
    	
    	root.setTop(title);
    	root.setCenter(ivPic);
        
        Scene scene = new Scene(root, 300, 400);
        
        primaryStage.setTitle("California");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
