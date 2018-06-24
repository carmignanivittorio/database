
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

public class visualizeTeams extends Application {

    @Override public void start(Stage stage) {

        long start; //initial instant
        
        Connection con = connDatabase.getConn(); //connection to establish
		
        Statement stmt=null; //statement to execute
        ResultSet rs=null; //and results to be displayed
              
        try{
            int row = 0;
			stage.setTitle("Football Manager application");
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));
			

			stmt = con.createStatement(); //creation of the statement
            start = System.currentTimeMillis();
					
			//Starting Year 
			grid.add(new Label("Starting year:"), 0, row);
			ComboBox sy = new ComboBox();
			rs = stmt.executeQuery("SELECT startingyear FROM season ORDER BY startingyear;");
			while (rs.next()){
				sy.getItems().add(rs.getString(1));
			}//while
			sy.getSelectionModel().selectFirst();
			grid.add(sy, 1, row++);
			
			//Button 1
			Button btn = new Button("GO");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(btn);
			grid.add(hbBtn, 1, row++);
			
			//Team
			ComboBox nameTeam = new ComboBox();
			grid.add(new Label("Teams:"), 0, row);
			grid.add(nameTeam, 1, row++);
			
			//Button 2
			Button btn2 = new Button("GO");
			HBox hbBtn2 = new HBox(10);
			hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn2.getChildren().add(btn2);
			grid.add(hbBtn2, 1, row++);

			//Category
			Label cat = new Label("");
			grid.add(new Label("Category: "), 0, row);
			grid.add(cat, 1, row++);
			
			//Technical Supervisor
			Label tech = new Label("");
			grid.add(new Label("Technical Supervisor: "), 0, row);
			grid.add(tech, 1, row++);
			
			//Coach
			grid.add(new Label("Coaches: "), 0, row);
			Label coach = new Label("");
			grid.add(coach, 1, row++);
			
			//Players
			grid.add(new Label("Players: "), 0, row);
			Label player = new Label("");
			grid.add(player, 1, row++);
			
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try{
						Connection con1 = connDatabase.getConn();
						Statement stmt1 = con1.createStatement();
						
						ResultSet rs1 = stmt1.executeQuery("SELECT name FROM team WHERE startingyearseason='"+sy.getSelectionModel().getSelectedItem().toString()+"' ORDER BY name;");
						
						while (rs1.next()){
							nameTeam.getItems().add(rs1.getString(1));
						}//while
						
						nameTeam.getSelectionModel().selectFirst();
						
						rs1.close();
						stmt1.close();
						con1.close();
						
												
					}catch(Exception e1){
						System.out.println(e1.getMessage());
					}//try-catch
				}//handle
				
			});//setOnAction
			
			btn2.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent e) {
					try{
						Connection con1 = connDatabase.getConn();
						Statement stmt1 = con1.createStatement();
						
						String nt = nameTeam.getSelectionModel().getSelectedItem().toString();
						String nty = sy.getSelectionModel().getSelectedItem().toString();
						
						//Category
						setLabelCategory(cat, "namecategory", "team", stmt1, nt, nty);
						
						//Technical Supervisor
						setLabel(tech, "usernametecsup", "manage", stmt1, nt, nty);
						
						//Coach
						setLabel(coach, "usernamecoach", "train", stmt1, nt, nty);
						
						//Players
						setLabel(player, "usernameplayer", "appertain", stmt1, nt, nty);
						
						stmt1.close();
						con1.close();
						
												
					}catch(Exception e1){
						System.out.println(e1.getMessage());
					}//try-catch
				}//handle
				
			});//setOnAction
			
			
			
			grid.setStyle("-fx-background-image: url('calciom.jpg'); ");	
			Scene scene = new Scene(grid, 566, 500);
			stage.setScene(scene);
			stage.show();
			
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
		
    }//start
	
	private void setLabel(Label l, String select, String from, Statement stmt, String nt, String nty) throws Exception{
		ResultSet rs = stmt.executeQuery("SELECT "+select+" FROM "+from+" WHERE nameteam='"+nt+"' AND startingyearteam='"+nty+"' ORDER BY "+select);
		String concat ="";
		while (rs.next()){
			concat+=rs.getString(1)+"\n";
		}//while
		l.setText(concat);
		rs.close();
		rs = null;
	}//setLabel
	
	private void setLabelCategory(Label l, String select, String from, Statement stmt, String nt, String nty) throws Exception{
		ResultSet rs = stmt.executeQuery("SELECT "+select+" FROM "+from+" WHERE name='"+nt+"' AND startingyearseason='"+nty+"' ORDER BY "+select);
		String concat ="";
		while (rs.next()){
			concat+=rs.getString(1)+"\n";
		}//while
		l.setText(concat);
		rs.close();
		rs = null;
	}//setLabel
	
}//prova
