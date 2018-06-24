
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class menu extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Football Manager");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(100);
        grid.setVgap(50);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Menu");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 32));
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        //Query 1
        Button q1 = new Button("Average Trainings bars");
        grid.add(q1, 0, 1);
        q1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
				Main.runApplication(new averageTrainingsBar());
            }
        });

        //Query 2
        Button q2 = new Button("Average trainings");
        grid.add(q2, 1, 1);
        q2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
				Main.runApplication(new averageTrainings());
            }
        });

        //Query 3
        Button q3 = new Button("Visualize Teams");
        grid.add(q3, 0, 2);
        q3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
				Main.runApplication(new visualizeTeams());

            }
        });

        //Query 4
        Button q4 = new Button("Insert Team");
        grid.add(q4, 1, 2);
        q4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
				Main.runApplication(new insertTeam());
            }
        });

        Scene scene = new Scene(grid, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
