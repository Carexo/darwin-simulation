package engine;

import model.Simulation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> simulationThreads = new ArrayList<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void awaitSimulationsEnd() {
        try {
            for (Thread thread : simulationThreads) {
                thread.join();
            }

            threadPool.shutdown();


            if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                System.err.println("Not all simulations finished in 10 secunds");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations) {
            threadPool.submit(simulation);
        }

        awaitSimulationsEnd();
    }

    public void interruptAllSimulations() {
        threadPool.shutdown();
    }
}
