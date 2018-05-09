package Chapter6;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lesson3 extends Application {
    Circle cir;
    Circle cir2;
    
    @Override
    public void start(Stage primaryStage) {
        
        Text title = new Text("Style Rules!");
        BorderPane pane = new BorderPane();
        
        Button btn = new Button();
        btn.setText("Click Me!");
        
        Button btn2 = new Button();
        btn2.setId("center");
        btn2.setText("Now Click Me!");
    //	btn2.setVisible(false);
        
        btn.setOnAction((ActionEvent e)-> {
        //    btn.setVisible(false);
            btn2.setVisible(true);
        //    cir2.setFill(Color.BLACK);
        //    cir.setFill(Color.DEEPPINK);
        });
        btn2.setOnAction((ActionEvent e)-> {
        //	cir.setFill(Color.BLACK);
            cir2.setFill(Color.DEEPPINK);
        //    btn.setVisible(true);
            btn2.setVisible(false);
        });
        cir = new Circle(100,100,40);
        cir2 = new Circle(100,100,60);
        cir.setId("center");
        cir2.setId("center");
        
        StackPane stack = new StackPane();
        StackPane stack2 = new StackPane();
        stack.getChildren().addAll(cir, btn);
        stack2.getChildren().addAll(cir2,btn2);
        pane.setTop(stack);
        pane.setCenter(stack2);
        
        Scene scene = new Scene(pane, 400,400);
        
        scene.getStylesheets().add("Chapter6/Lesson3.css");
        
        primaryStage.setTitle("Style Rules");
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
