import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import presenter.ConfigurationSimulationPresenter;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/configurationSimulation.fxml"));
        BorderPane viewRoot = loader.load();

        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        ConfigurationSimulationPresenter presenter = loader.getController();

        primaryStage.setOnCloseRequest(event -> {
            presenter.onConfigurationSimulationApplicationClose();

            Platform.exit();
        });
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin Evolution Simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
    }
}
