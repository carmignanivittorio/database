

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart;

import java.sql.*;

public class averageTrainingsBar extends Application {

    @Override public void start(Stage stage) {
		
        long start; //initial instant
        
        Connection con = connDatabase.getConn(); //connection to establish
		
		Statement stmt=null; //statement to execute
        ResultSet rs=null; //and results to be displayed
              
        try{
            stmt = con.createStatement(); //creation of the statement
			XYChart.Series series1 = new XYChart.Series();
            
			start = System.currentTimeMillis();
            rs = stmt.executeQuery("SELECT drill.mainobjective, ROUND ( SUM(drill.duration)*100.0/SUM(SUM(drill.duration)) over(), 2 ) FROM attend1 INNER JOIN drill ON attend1.iddrill = drill.iddrill INNER JOIN player ON player.username = attend1.usernameplayer WHERE player.username = 'zanna' GROUP BY drill.mainobjective;");
			connDatabase.printTimeQuery(start);

            System.out.format("%20S%16S\n", "name", "Average");

            connDatabase.printTrat(20*2+16);
            while (rs.next()){
                String name = rs.getString(1);
                double avg = rs.getDouble(2);
				series1.getData().add(new XYChart.Data(name, avg));
                System.out.format("%20s%16f\n", name, avg);
            }//while
			connDatabase.printTrat(20*2+16);
			
			connDatabase.createBar(series1, stage,"Types of drill","Number of drill", "Drills done");
			
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
