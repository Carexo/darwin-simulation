package engine;

import model.simulation.Simulation;

import java.util.*;


public class SimulationEngine {
    private final Map<UUID, Thread> threads = new HashMap<>();
    private final Map<UUID, Simulation> simulations = new HashMap<>();


    public SimulationEngine() {
    }

    public void awaitSimulationsEnd() {
        try {
            for (Thread thread : threads.values()) {
                if (!thread.isAlive()) {
                    continue;
                }

                thread.join();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void addSimulation(Simulation simulation) {
        Thread thread = new Thread(simulation);
        threads.put(simulation.getSimulationId(), thread);
        simulations.put(simulation.getSimulationId(), simulation);
        thread.start();
    }

    public void stopAllSimulations() {
        simulations.values().forEach(Simulation::resume);
        threads.values().forEach(Thread::interrupt);
    }

    public void stopSingleSimulation(Simulation simulation) {
        simulation.resume();
        threads.get(simulation.getSimulationId()).interrupt();
    }
}
