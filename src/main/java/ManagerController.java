import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


public class ManagerController {
    private MapManager model;
    @FXML
    private ListView<String> keys;
    @FXML
    private Button addButton;
    @FXML
    private TextField key;
    @FXML
    private TextField value;
    @FXML
    private Label gotValue;
    @FXML
    private TextField keyToGetValue;
    @FXML
    private Button getButton;
    @FXML
    private Button deleteButton;

    @FXML
    public void initialize(){
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> addNewEntry());
        getButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String value = model.getServer().getState().get(keyToGetValue.getText());
            gotValue.setText(value);
        });
        deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> model.getServer().getState().remove(keys.getSelectionModel().getSelectedItem()));
        keyToGetValue.textProperty().bind(keys.getSelectionModel().selectedItemProperty());
    }

    public void setModel(MapManager model) {
        this.model = model;
    }

    public ListView<String> getKeys() {
        return keys;
    }

    private void addNewEntry() {
        model.getServer().getState().put(key.getText(), value.getText());
    }


}
