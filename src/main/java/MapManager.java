import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MapManager {
    private Scene scene;
    private Pane rootLayout;
    private Server server;
    private ManagerController controller;

    public MapManager(){
        try {
            FXMLLoader loader = new FXMLLoader(MapManager.class.getResource("map_manager.fxml"));
            rootLayout = loader.load();
            controller = loader.getController();
            controller.setModel(this);
            this.scene = new Scene(rootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server = new Server("224.0.0.1", this);
    }

    public Scene getScene() {
        return scene;
    }

    public ListView<String> getListView(){
        return controller.getKeys();
    }

    public Server getServer() {
        return server;
    }
}
