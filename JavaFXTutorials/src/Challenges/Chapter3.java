package Challenges;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Chapter3 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Random rand = new Random();
        int pt1X = 10, pt1Y = 10, pt2X = 10, pt2Y = 40, pt3X = 40, pt3Y = 40;
        
        for(int i = 0; i < 5; i++) {
        	Polygon p = new Polygon(pt1X+(40*i), pt1Y, pt2X+(40*i), pt2Y, pt3X+(40*i), pt3Y);
        	
        	int answer = rand.nextInt(5);
        	switch(answer)
        	{
            	case 0: p.setFill(Color.FIREBRICK); p.setStroke(Color.GREEN);
            	break;
            	case 1: p.setFill(Color.GREEN); p.setStroke(Color.FIREBRICK);
            	break;
            	case 2: p.setFill(Color.ORANGE); p.setStroke(Color.BLUE);
            	break;
            	case 3: p.setFill(Color.BLUE); p.setStroke(Color.ORANGE);
            	break;
            	case 4: p.setFill(Color.BLACK); p.setStroke(Color.BLACK);
            	break;
        	}
        	
        	root.getChildren().add(p);
        }
        
        Scene scene = new Scene(root, 400, 400);
        
        primaryStage.setTitle("Hello World!");
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
