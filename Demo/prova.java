import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.chart.*;

import java.util.*;
import java.sql.*;


public class prova extends Application {

    @Override public void start(Stage stage) {

        long start; //initial instant
        
        Connection con = connDatabase.getConn(); //connection to establish
		
        Statement stmt=null; //statement to execute
        ResultSet rs=null; //and results to be displayed
              
        try{
            stmt = con.createStatement(); //creation of the statement
           
            start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT coach.name, coach.surname, COUNT(*) FROM coach INNER JOIN drill ON drill.usernamecoach = coach.username GROUP BY coach.username");
            
			connDatabase.printTimeQuery(start);

            System.out.format("%20S%20S%16S\n", "name", "surname", "numberofdrills");
			
			List<PieChart.Data> data = new ArrayList<PieChart.Data>();
			
            connDatabase.printTrat(20*2+16);

            while (rs.next()){
                String name = rs.getString(1);
                String surname = rs.getString(2);
                int numberofdrills = rs.getInt(3);
                data.add(new PieChart.Data(name+" "+surname, numberofdrills));
                System.out.format("%20s%20s%16s\n", name, surname, numberofdrills);
            }//while
			
            connDatabase.printTrat(20*2+16);
			
            connDatabase.createPieChart(new PieChart(FXCollections.observableArrayList(data)), stage, "Drills composition");

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
