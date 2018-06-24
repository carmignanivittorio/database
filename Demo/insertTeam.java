
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

import java.sql.*;

public class insertTeam extends Application {

    Map<String, ObservableValue<Boolean>> map = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
		
		long start; //initial instant
		int row = 1;
        
        Connection con = connDatabase.getConn(); //connection to establish
				
        Statement stmt=null; //statement to execute
        ResultSet rs=null; //and results to be displayed
              
        try{
            stmt = con.createStatement(); //creation of the statement          
            start = System.currentTimeMillis();
			
			rs = stmt.executeQuery("SELECT * FROM team");
						while (rs.next()){
							System.out.println(rs.getString(1));
						}//while

			primaryStage.setTitle("Football Manager application");
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));

			Text scenetitle = new Text("Insert data team");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 32));
			grid.add(scenetitle, 0, 0, 2, 1);
			
			//Starting Year 
			grid.add(new Label("Starting year:"), 0, row);
			ComboBox sy = new ComboBox();
			rs = stmt.executeQuery("SELECT startingyear FROM season ORDER BY startingyear;");
			while (rs.next()){
				sy.getItems().add(rs.getString(1));
			}//while			
			sy.getSelectionModel().selectFirst();
			grid.add(sy, 1, row);
			row++;
			
			//Team
			grid.add(new Label("Name:"), 0, row);
			TextField nameField = new TextField();
			grid.add(nameField, 1, row);
			row++;
			
			//Category
			grid.add(new Label("Category:"), 0, row);
			ComboBox categoryField = new ComboBox();
			rs = stmt.executeQuery("SELECT name FROM category ORDER BY name;");
			while (rs.next()){
				categoryField.getItems().add(rs.getString(1));
			}//while
			categoryField.getSelectionModel().selectFirst();
			grid.add(categoryField, 1, row);
			row++;

			//Coach 1
			grid.add(new Label("Coach:"), 0, row);
			ComboBox coachField = new ComboBox();
			rs = stmt.executeQuery("SELECT username FROM coach ORDER BY surname;");
			while (rs.next()){
				coachField.getItems().add(rs.getString(1));
			}//while
			coachField.getSelectionModel().selectFirst();
			grid.add(coachField, 1, row);
			row++;


			//Technical Supervisor
			grid.add(new Label("Technical Supervisor:"), 0, row);
			ComboBox techSupField = new ComboBox();
			rs = stmt.executeQuery("SELECT username FROM technicalsupervisor ORDER BY username;");
			while (rs.next()){
				techSupField.getItems().add(rs.getString(1));
			}//while
			techSupField.getSelectionModel().selectFirst();
			grid.add(techSupField, 1, row);
			row++;
					
			//Player
			ListView<String> players = new ListView<>();
			players.setPrefSize(250, 800);
			players.setEditable(true);
			players.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			rs = stmt.executeQuery("SELECT username FROM player ORDER BY username;");
			while (rs.next()){
				String us = rs.getString(1);
				map.put(us, new SimpleBooleanProperty(false));
				players.getItems().add(us);
			}//while

			// Create a Callback object
			Callback<String, ObservableValue<Boolean>> itemToBoolean = (String item) -> map.get(item);

			// Set the cell factory to my CheckBoxListCell implementation
			players.setCellFactory(lv -> new insertTeam.MyCell(itemToBoolean));
			grid.add(new Label("Players:"), 0, row);
			grid.add(players, 1, row);
			row++;
			
			Button btn = new Button("GO");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(btn);
			grid.add(hbBtn, 1, row);			
			row++;
			
			final Text actiontarget = new Text();
			grid.add(actiontarget, 1, row);
			
			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent e) {
					try{
						Connection con1 = connDatabase.getConn();
						Statement stmt1 = con1.createStatement();
						
						String insy = sy.getSelectionModel().getSelectedItem().toString();
						String inname = nameField.getText();
						String innamesy = "INSERT INTO team(name, startingyearseason, namecategory) VALUES ('"+inname+"','"+insy+"','"+categoryField.getSelectionModel().getSelectedItem().toString()+"'); ";
						String intech = "INSERT INTO manage(usernametecsup, nameteam, startingyearteam) VALUES ('"+techSupField.getSelectionModel().getSelectedItem().toString()+"','"+inname+"','"+insy+"'); ";
						String incoach = "INSERT INTO train(usernamecoach, nameteam, startingyearteam) VALUES ('"+coachField.getSelectionModel().getSelectedItem().toString()+"','"+inname+"','"+insy+"'); ";
						String inplayer = "INSERT INTO appertain(usernameplayer, nameteam, startingyearteam) VALUES "+printSelection(inname,insy)+"; ";
						stmt1.executeUpdate("BEGIN;"+innamesy+intech+ incoach+ inplayer +" COMMIT;");
						
						actiontarget.setFill(Color.GREEN);
						actiontarget.setText("Query executed succesfully!");
						
					}catch(Exception e1){
						actiontarget.setFill(Color.FIREBRICK);
						actiontarget.setText("Problem: "+e1.getMessage());
					}
				}
			});
			
			grid.setStyle("-fx-background-image: url('loghi.jpg'); ");
			Scene scene = new Scene(grid, 800, 1200);
			primaryStage.setScene(scene);
			primaryStage.show();

            rs.close();
            stmt.close();
            con.close();

        }catch(Exception e){
            System.out.println("Unable to release the resources: "+e.getMessage());
        }finally{
            rs = null;
            stmt = null;
            con = null;
        }//try-catch-finally
        
    }

    public String printSelection(String nameTeam, String startingYear) {	
		String values="";
        Object[] keys =  map.keySet().toArray();
		int i;
        for(i=0; i<keys.length-1;i++){
            ObservableValue<Boolean> value = map.get(keys[i].toString());
            if (value.getValue()) {
                values += "('"+keys[i].toString()+"', '"+nameTeam+"', '"+startingYear+"')";
				break;
            }//if
        }//for
		
		for(int j=i+1; j<keys.length-1;j++){
            ObservableValue<Boolean> value = map.get(keys[j].toString());
            if (value.getValue())
                values += ",('"+keys[j].toString()+"', '"+nameTeam+"', '"+startingYear+"')";
        }//for	
		
		return values;
    }

    public class MyCell extends CheckBoxListCell<String> {
        public MyCell(Callback<String, ObservableValue<Boolean>> getSelectedProperty){
            super(getSelectedProperty);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
        }
    }
}
