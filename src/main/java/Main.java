import javafx.application.Application;
import model.Configuration;
import model.elements.TidesMap;

public class Main {
    public static void main(String[] args) {
        try {
            Application.launch(SimulationApp.class, args);
        } catch (IllegalStateException e) {
            System.out.println("Cannot launch application");
        }
//        Configuration config = new Configuration();
//        try{
//            TidesMap map = new TidesMap(config);
//        }catch(Exception e){
//            e.printStackTrace();
//        }


    }

}