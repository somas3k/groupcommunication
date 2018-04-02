import javafx.application.Application;
import javafx.stage.Stage;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private Stage stage;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack","true");
        launch(args);
        //new Server("224.0.0.1");
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setTitle("DistributedMap");
        stage.show();
        stage.setScene(new MapManager().getScene());
    }
}
