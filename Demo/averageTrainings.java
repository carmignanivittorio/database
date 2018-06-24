

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.chart.*;

import java.util.*;
import java.sql.*;


public class averageTrainings extends Application {

    @Override public void start(Stage stage) {
		
		long start; //initial instant
        
        Connection con = connDatabase.getConn(); //connection to establish
				
        Statement stmt=null; //statement to execute
        ResultSet rs=null; //and results to be displayed
              
        try{
            stmt = con.createStatement(); //creation of the statement
           
            start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT drill.mainobjective, ROUND ( SUM(drill.duration)*100.0/SUM(SUM(drill.duration)) over(), 2 ) FROM attend1 INNER JOIN drill ON attend1.iddrill = drill.iddrill INNER JOIN player ON player.username = attend1.usernameplayer WHERE player.username = 'zanna' GROUP BY drill.mainobjective;");
            
			connDatabase.printTimeQuery(start);

            System.out.format("%20S%16S\n", "name", "Average");
			
			List<PieChart.Data> data = new ArrayList<PieChart.Data>();
			
            connDatabase.printTrat(20*2+16);

            while (rs.next()){
                String name = rs.getString(1);
                double avg = rs.getDouble(2);
                data.add(new PieChart.Data(name, avg));
                System.out.format("%20s%16f\n", name, avg);
            }//while
			
            connDatabase.printTrat(20*2+16);
			
            connDatabase.createPieChart(new PieChart(FXCollections.observableArrayList(data)), stage,"Average Training");

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
	

}//prova
