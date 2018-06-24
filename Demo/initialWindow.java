
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class initialWindow extends Application {
    @Override
    public void start(Stage stage) {
        //Creating an image
        Image image = new Image("file:Logo.png", true);

        //Instantiating the Rectangle class
        Rectangle rectangle = new Rectangle();

        //Instantiating the ImageInput class
        ImageInput imageInput = new ImageInput();

        //Setting the position of the image
        imageInput.setX(70);
        imageInput.setY(60);

        //Setting source for image input
        imageInput.setSource(image);

        //Applying image input effect to the rectangle node
        rectangle.setEffect(imageInput);

        //Creating a Group object
        Group root = new Group(rectangle);

        //Creating a scene object
        Scene scene = new Scene(root, 1000, 300);

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.hide();
                Main.runApplication(new login());
            }
        });

        //Setting title to the Stage
        stage.setTitle("Sample Application");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

    }
    public static void main(String args[]){
        launch(args);
    }
}
