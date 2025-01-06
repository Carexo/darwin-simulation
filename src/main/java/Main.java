import model.Configuration;
import model.Simulation;
import model.map.EarthMap;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);

        Simulation simulation = new Simulation(map, config);
        simulation.run();
    }
}