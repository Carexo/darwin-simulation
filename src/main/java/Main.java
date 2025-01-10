import javafx.application.Application;
import model.Configuration;
import model.Simulation;
import model.map.EarthMap;

public class Main {
    public static void main(String[] args) {
        try {
            Application.launch(SimulationApp.class, args);
        } catch (IllegalStateException e) {
            System.out.println("Cannot launch application");
        }


    }
}