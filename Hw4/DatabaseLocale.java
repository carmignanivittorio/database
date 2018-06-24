import java.util.Scanner;
import java.util.Formatter;
import java.sql.*;
import javax.sql.*;

public class DatabaseLocale{
  public static void main(String[] args){
    try{
      Class.forName("org.postgresql.Driver"); //load postgreSQL driver
    }catch(ClassNotFoundException e) {
      System.out.println("Driver not found: "+e.getMessage());
      System.exit(-1);
    }
    System.out.println("----------- Welcome to dbsport -----------");
    long start; //initial instant
    long end; //finishing instant
    String url="jdbc:postgresql://localhost/dbSport"; //url of the database to reach
    String login="postgres"; //username
    String password="1234"; //password
    Connection con=null; //connection to establisgh
    Statement stmt=null; //statement to execute
    ResultSet rs=null; //and results to be displayed
    int selection=-5; //query number
    boolean quit=false; //wheter to exit or ask again
    boolean resultspresent=false; //if there are results to be displayed
    try{
      start = System.currentTimeMillis();
      con = DriverManager.getConnection(url,login,password); // try to connect to the database
      end = System.currentTimeMillis();
      end-=start;
      System.out.println("Successfully connected to the database in "+end+"ms");
    }catch(SQLException e){
      System.out.println("Connection failed: "+e.getMessage());
      System.exit(-1);
    }
    while(!quit){
      System.out.println("----------------------------");
      System.out.println("0 to quit the execution");
      System.out.println("1: Visualize all the players stored in the dataset");
      System.out.println("2: Visualize all the players who live in Padova");
      System.out.println("3: Given a duration, show the number of drills per 'aim'");
      System.out.println("4: Visualize Federation ID, role and email of players for a specific team");
      System.out.println("5: Visualize the number of drills per coach");
      System.out.println("6: Visualize all the drills available for a specific category");
      System.out.println("7: Search drills with a specific keyword ");
      System.out.println("8: Given a squad, visualize the number of trainings with total and average duration");
      System.out.println("----------------------------");
      Scanner in=new Scanner(System.in);
      String s=null;
      while(selection<0||selection>8){
        System.out.print("Please insert a positive integer in the range (0,8): ");
        try{ selection=Integer.parseInt(in.next());}
        catch(Exception e){}
      }
      in.nextLine();
      try{
          stmt = con.createStatement(); //creation of the statement
          switch(selection){
            case 0:
              quit=true;
              break;
            case 1: //Visualize all the players stored in the dataset
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT username, name, surname, federationid, birthdate FROM player");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%20S%20S%16S%16S%16S\n", "username", "name", "surname","federationid","birthdate");
              for(int i=0;i<(20*2+16*3);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the query results
                String username = rs.getString(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                int federationid = rs.getInt(4);
                Date birthdate = rs.getDate(5);
                System.out.format("%20s%20s%16s%16d%16s\n", username, name, surname,federationid,birthdate);
              }
              for(int i=0;i<(20*2+16*3);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 2: //Visualize all the players who live in Padova
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT username, name, surname, federationid, birthdate FROM player WHERE player.city = 'padova'");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%20S%20S%16S%16S%16S\n", "username", "name", "surname","federationid","birthdate");
              for(int i=0;i<(20*2+16*3);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the query results
                String username = rs.getString(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                int federationid = rs.getInt(4);
                Date birthdate = rs.getDate(5);
                System.out.format("%20s%20s%16s%16d%16s\n", username, name, surname,federationid,birthdate);
              }
              for(int i=0;i<(20*2+16*3);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 3: //Given a duration, show the number of drills per 'aim'
              int length=-1; //the maximum duration
              while(length<0){
                System.out.print("Insert the maximum duration in minutes (e.g. 30): ");
                try{ length=Integer.parseInt(in.next());}
                catch(Exception e){}
              }
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT mainobjective, COUNT(*) FROM drill WHERE duration<="+length+" GROUP BY mainobjective");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%50S%30S\n", "mainobjective", "drills_grouped_by_mainobj");
              for(int i=0;i<(50+30);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the query results
                String mainobjective = rs.getString(1);
                int drills = rs.getInt(2);
                System.out.format("%50s%30d\n", mainobjective, drills);
              }
              for(int i=0;i<(50+30);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 4: //Visualize Federation ID, role and email of players for a specific team
              rs = stmt.executeQuery("SELECT name FROM team WHERE startingyearseason = '2017-07-01'");
              System.out.print("Available teams for the current season (pick one of these):\n");
              System.out.format("%20S\n", "team");
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the teams to be chosen
                String squad = rs.getString(1);
                System.out.format("%20s\n", squad);
              }
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\ntype in name: ");
              s=in.nextLine(); //the squad to be displayed
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT name, surname, federationid, primaryrole, email FROM player INNER JOIN appertain ON appertain.usernameplayer = player.username WHERE appertain.nameteam = '"+s.toLowerCase()+"'");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%20S%20S%15S%30S%30S\n", "name", "surname", "federationid", "primaryrole", "email");
              for(int i=0;i<(20*2+15+30*2);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              resultspresent=false;; //indicate whether there are results
              while (rs.next()){
                String name = rs.getString(1);
                String surname = rs.getString(2);
                int federationid= rs.getInt(3);
                String primaryrole = rs.getString(4);
                String email = rs.getString(5);
                System.out.format("%20s%20s%15d%30s%30s\n", name, surname, federationid, primaryrole, email);
                resultspresent=true;
              }
              if(!resultspresent) System.out.println("No results for the specified squad");
              for(int i=0;i<(20*2+15+30*2);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 5: //Visualize the number of drills per coach
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT coach.name, coach.surname, COUNT(*) FROM coach INNER JOIN drill ON drill.usernamecoach = coach.username GROUP BY coach.username");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%20S%20S%16S\n", "name", "surname", "numberofdrills");
              for(int i=0;i<(20*2+16);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing query results
                String name = rs.getString(1);
                String surname = rs.getString(2);
                int numberofdrills = rs.getInt(3);
                System.out.format("%20s%20s%16s\n", name, surname, numberofdrills);
              }
              for(int i=0;i<(20*2+16);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 6: //Visualize all the drills available for a specific category
              rs = stmt.executeQuery("SELECT name FROM category");
              System.out.print("Available categories to be chosen:\n");
              System.out.format("%20S\n", "category");
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the categories to be chosen
                String category = rs.getString(1);
                System.out.format("%20s\n", category);
              }
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\ntype in name: ");
              s=in.nextLine(); //the category to be displayed
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT name, mainobjective, equipment FROM drill INNER JOIN suit ON drill.iddrill = suit.iddrill WHERE	suit.namecategory = '"+s.toLowerCase()+"' AND drill.shareable = TRUE ORDER BY 	drill.name");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%30S%50S%30S\n", "name", "mainobjective", "equipment");
              for(int i=0;i<(30*2+50);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              resultspresent=false;;
              while (rs.next()){
                String name = rs.getString(1);
                String mainobjective = rs.getString(2);
                String equipment = rs.getString(3);
                System.out.format("%30s%50s%30s\n", name, mainobjective, equipment);
                resultspresent=true;
              }
              if(!resultspresent) System.out.println("No results for the specified category");
              for(int i=0;i<(30*2+50);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 7: //Search drills with a specific keyword
              rs = stmt.executeQuery("SELECT * FROM keyword");
              System.out.print("Available keywords to be chosen:\n");
              System.out.format("%20S\n", "keyword");
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the keywords to be chosen
                String keyword = rs.getString(1);
                System.out.format("%20s\n", keyword);
              }
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              System.out.print("type in keyword: ");
              s=in.nextLine(); //the keyword to be displayed
              start = System.currentTimeMillis();
              /*the following statement needs to be reset, we use prepareStatement*/
              rs = con.prepareStatement("SELECT drill.name, drill.description, mainobjective, equipment FROM drill INNER JOIN describe ON drill.iddrill = describe.iddrill WHERE describe.namekeyword = '"+s.toLowerCase()+"' AND drill.shareable = TRUE ORDER BY 	drill.name", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              int cl[]=new int[4]; //cl[i] as length of column i+1
              cl[0]=10; //initial length
              cl[1]=17; //initial length
              cl[2]=19; //initial length
              cl[3]=15; //initial length
              while (rs.next()){ //calculating column lenths
                for(int i=0;i<4;i++){
                  String phrase=rs.getString(i+1);
                  if(phrase.length()>cl[i]) cl[i]=phrase.length()+5;
                }
              }
              System.out.format("%"+cl[0]+"S%"+cl[1]+"S%"+cl[2]+"S%"+cl[3]+"S\n", "name", "description","mainobjective", "equipment");
              for(int i=0;i<(cl[0]+cl[1]+cl[2]+cl[3]);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              rs.beforeFirst(); //reset query cursor
              resultspresent=false;;
              while (rs.next()){ //printing query results
                String name = rs.getString(1);
                String description = rs.getString(2);
                String mainobjective = rs.getString(3);
                String equipment = rs.getString(4);
                System.out.format("%"+cl[0]+"S%"+cl[1]+"S%"+cl[2]+"S%"+cl[3]+"S\n", name, description, mainobjective, equipment);
                resultspresent=true;
              }
              if(!resultspresent) System.out.println("No results for the specified keyword");
              for(int i=0;i<(cl[0]+cl[1]+cl[2]+cl[3]);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
            case 8: //Given a squad, visualize the number of trainings with total and average duration
              rs = stmt.executeQuery("SELECT name FROM team WHERE startingyearseason = '2017-07-01'");
              System.out.print("Available teams for the current season:\n");
              System.out.format("%20S\n", "name");
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              while (rs.next()){ //printing the teams to be chosen
                String squad = rs.getString(1);
                System.out.format("%20s\n", squad);
              }
              for(int i=0;i<(20);i++){
                System.out.print("-");
              }
              System.out.print("\ntype in name: ");
              s=in.nextLine(); //the squad to be displayed
              start = System.currentTimeMillis();
              rs = stmt.executeQuery("SELECT COUNT(*), SUM(training.duration), AVG(training.duration) FROM training WHERE nameteam='"+s.toLowerCase()+"' GROUP BY training.nameteam");
              end = System.currentTimeMillis();
              end-=start;
              System.out.format("%S%d%s\n","Query completely executed in ",end," ms");
              System.out.format("%17S%13S%13S\n", "numberoftrainings", "sumduration","avgduration");
              for(int i=0;i<(13*2+17);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              resultspresent=false;;
              while (rs.next()){
                int numberodtrainings = rs.getInt(1);
                int sumduration = rs.getInt(2);
                int avgduration = rs.getInt(3);
                System.out.format("%17d%13d%13d\n", numberodtrainings, sumduration, avgduration);
                resultspresent=true;
              }
              if(!resultspresent) System.out.println("No results for the specified squad");
              for(int i=0;i<(13*2+17);i++){
                System.out.print("-");
              }
              System.out.print("\n");
              break;
          }
          /* restoring default values of variables*/
          selection=-1;
          rs = null;
      }catch(SQLException e){
        System.out.println("Unable to execute statement: "+e.getMessage());
      }
    }
    try{ //close the connection
      rs.close();
      stmt.close();
      con.close();
    }
    catch(SQLException e) {
      System.out.println("Unable to release the resources: "+e.getMessage());
    }
    catch(NullPointerException e){
      System.out.println("Bye!");
      System.exit(0);
    }
    finally{
      rs = null;
      stmt = null;
      con = null;
    }
  }
}
