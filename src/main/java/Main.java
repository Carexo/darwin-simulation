import javafx.application.Application;
import model.Configuration;
import model.map.TidesMap;

public class Main {
    public static void main(String[] args) {
        try {
            Application.launch(SimulationApp.class, args);
        } catch (IllegalStateException e) {
            System.out.println("Cannot launch application");
        }
    }

}