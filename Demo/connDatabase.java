import java.sql.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.awt.Label;
import java.awt.Color;


public class connDatabase{
	
	public static Connection getConn(){
		loadDriver();
		Connection conn = null;
		try{
            long start = System.currentTimeMillis();
			String url="jdbc:postgresql://localhost:5433/dbms1718?currentSchema=footballmanager"; //url of the database to reach
            String login="webdb"; //username
			String password="webdb"; //password
            conn = DriverManager.getConnection(url,login,password);
			System.out.println("Successfully connected to the database in "+(System.currentTimeMillis()-start)+"ms");
        }catch(SQLException e){
            System.out.println("Connection failed: "+e.getMessage());
            System.exit(-1);
        }
		return conn;
	}//getConn
	
	public static void loadDriver(){
		try{
            Class.forName("org.postgresql.Driver"); //load postgreSQL driver
        }catch(ClassNotFoundException e) {
            System.out.println("Drivery not found: "+e.getMessage());
            System.exit(-1);
        }//try-catch
	}//loadDriver
		
	public static void printTrat(int n){
        for(int i=0;i<n;i++)
            System.out.print("-");
        System.out.print("\n");
    }//printTrat

    public static void printTimeQuery(long start){
        System.out.format("%S%d%s\n","Query completely executed in ",System.currentTimeMillis()-start," ms");
    }//printTrat
	
	public static void createBar(XYChart.Series s, Stage stage, String labelX, String labelY, String title){
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
		bc.setTitle(title);
		xAxis.setLabel(labelX);       
		yAxis.setLabel(labelY);
		bc.setLegendVisible(false);
		Scene scene  = new Scene(bc,800,600);
		bc.getData().addAll(s);
		stage.setTitle("Bar Chart");
		stage.setScene(scene);
		
		
		stage.show();
		
	}//createBar
	
	public static void createPieChart(PieChart p, Stage stage, String title){
		Scene scene = new Scene(new Group());
		stage.setTitle("Pie chart");
		stage.setWidth(500);
		stage.setHeight(500);
		p.setClockwise(true);
		p.setTitle(title);
		((Group) scene.getRoot()).getChildren().add(p);
		stage.setScene(scene);
		

		stage.show();
		
	}//createGraph
	

}//connDatabase