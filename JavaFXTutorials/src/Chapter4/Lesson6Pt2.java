package Chapter4;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Lesson6Pt2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
         Label response = new Label("");
        Label title = new Label("Contact List Using a TableView\n");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.CADETBLUE);
       
        
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(root, 450, 450);
        
        ObservableList<Lesson6Pt1> contactList = FXCollections.observableArrayList(
                new Lesson6Pt1("Peggy", "Fisher", "717-555-1212"), 
                new Lesson6Pt1("Jim", "Freed", "441-456-1345"), 
                new Lesson6Pt1("Pat", "Keegan", "717-363-1432"), 
                new Lesson6Pt1("Jane", "Slattery", "441-478-4488"), 
                new Lesson6Pt1("Cy", "Young", "970-554-1265"), 
                new Lesson6Pt1("Rob", "Jones", "570-655-1587"), 
                new Lesson6Pt1("Carol", "King", "215-547-5958"), 
                new Lesson6Pt1("Bob", "Kauffman", "215-456-6345"), 
                new Lesson6Pt1("Gloria", "Shilling", "717-785-6092"), 
                new Lesson6Pt1("Bill", "Sigler", "441-444-1345")
                );
    
        TableView<Lesson6Pt1> tvLesson6Pt1s;
        
        tvLesson6Pt1s = new TableView<Lesson6Pt1>(contactList);
        
        TableColumn<Lesson6Pt1, String> fName = new TableColumn<>("First Name");
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tvLesson6Pt1s.getColumns().add(fName);
        
        TableColumn<Lesson6Pt1, String> lName = new TableColumn<>("Last Name");
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tvLesson6Pt1s.getColumns().add(lName);
        
        TableColumn<Lesson6Pt1, String> cell = new TableColumn<>("Cell Phone Number");
        cell.setCellValueFactory(new PropertyValueFactory<>("cellPhone"));
        tvLesson6Pt1s.getColumns().add(cell);
        
        tvLesson6Pt1s.setPrefWidth(300);
        tvLesson6Pt1s.setPrefHeight(300);
        
        TableView.TableViewSelectionModel<Lesson6Pt1> tvSelLesson6Pt1 = 
                tvLesson6Pt1s.getSelectionModel();
        
        tvSelLesson6Pt1.selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> changed, 
                    Number oldVal, Number newVal) {
                int index = (int)newVal;
                response.setText("The cell number for the contact selected is "
                        +contactList.get(index).getCellPhone());
            }
        });
        
        response.setFont(Font.font("Arial", 14));
        root.getChildren().addAll(title,tvLesson6Pt1s, response);
        primaryStage.setTitle("Contact List");
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
