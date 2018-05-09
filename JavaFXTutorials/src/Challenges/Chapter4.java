package Challenges;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chapter4 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox btns = new VBox();
        btns.setId("vbox");
        
        Label lbl = new Label();
        lbl.setId("lbl");
        
        Button btn1 = new Button("Button 1");
        Button btn2 = new Button("Button 2");
        Button btn3 = new Button("Button 3");
        
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) ->{
        	lbl.setText("Good job you clicked a button!");
        });
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) ->{
        	lbl.setText("Yeah, that's right! You clicked a button!");
        });
        btn3.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) ->{
        	lbl.setText("Teach that button a lesson!");
        });
        
        btns.getChildren().addAll(btn1, btn2, btn3);
        btns.setSpacing(20);
        
        root.setLeft(btns);
        root.setCenter(lbl);
        
        Scene scene = new Scene(root, 400, 400, Color.WHITE);
        scene.getStylesheets().add("Challenges/Chapter4.css");
        
        MenuBar menuBar = new MenuBar();
    	menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    	root.setTop(menuBar);
        
    	Menu fileMenu = new Menu("File");
    	MenuItem newMenuItem = new MenuItem("New");
    	MenuItem saveMenuItem = new MenuItem("Save");
    	MenuItem printMenuItem = new MenuItem("Print");
    	MenuItem exitMenuItem = new MenuItem("Exit");
    	
    	fileMenu.getItems().addAll(newMenuItem, saveMenuItem, printMenuItem,
    			new SeparatorMenuItem(), exitMenuItem);
    
    	exitMenuItem.setOnAction(actionEvent->Platform.exit());

    	menuBar.getMenus().addAll(fileMenu);
        
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
