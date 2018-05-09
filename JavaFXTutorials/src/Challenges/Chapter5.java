package Challenges;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Chapter5 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    	String jan = "January";
    	String feb = "February";
    	String mar = "March";
    	
    	CategoryAxis xAxis = new CategoryAxis();
    	NumberAxis yAxis = new NumberAxis();
    	BarChart<String, Number> chart = 
    			new BarChart<String, Number>(xAxis, yAxis);
    	
    	chart.setTitle("Revenue Q1");
    	xAxis.setLabel("Month");
    	yAxis.setLabel("Sales");
    	
    	XYChart.Series<String, Number> sm1 = new XYChart.Series<String, Number>();
    	sm1.setName("Salesman 1");
    	sm1.getData().add(new XYChart.Data<String, Number>(jan, 500));
    	sm1.getData().add(new XYChart.Data<String, Number>(feb, 350));
    	sm1.getData().add(new XYChart.Data<String, Number>(mar, 650));

    	XYChart.Series<String, Number> sm2 = new XYChart.Series<String, Number>();
    	sm2.setName("Salesman 2");
    	sm2.getData().add(new XYChart.Data<String, Number>(jan, 650));
    	sm2.getData().add(new XYChart.Data<String, Number>(feb, 350));
    	sm2.getData().add(new XYChart.Data<String, Number>(mar, 500));
    	
        FlowPane root = new FlowPane();
        root.getChildren().add(chart);
        chart.getData().addAll(sm1, sm2);
        
        Scene scene = new Scene(root, 500, 500);
        
        primaryStage.setTitle("Q1 Revenue");
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
