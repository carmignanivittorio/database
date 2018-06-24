
import javafx.application.Platform;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;

public class Main {

    public static void main(String[] args) {

        runApplication(new menu());

    }//main

    public static void runApplication(Application a){

        try{

            new JFXPanel();

            Platform.runLater(new Runnable() {
                @Override
                public void run(){
                    try{a.start(new Stage());
                    }catch(Exception e){};
                }//run
            });

        }catch (Exception e) {
            e.printStackTrace();
        }//try-catch

    }//runApplication

}//Main
