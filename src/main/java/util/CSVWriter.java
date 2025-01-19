package util;

import model.simulation.Simulation;
import model.simulation.SimulationChangeListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class CSVWriter implements SimulationChangeListener {
    @Override
    public void onSimulationChange(Simulation simulation) {
        File logFile = createLogFile(simulation.getSimulationId());
        boolean isFirstLine = isFirstLine(logFile);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            if (isFirstLine) {
                writer.append(simulation.getStatisticSimulation().getCSVHeaders())
                        .append(System.lineSeparator());
            }
            writer.append(simulation.getStatisticSimulation().getCSVData())
                    .append(System.lineSeparator());
        } catch (IOException ex) {
            System.out.printf("Failed to write to a csv file: %s, with error: %s\n", logFile.getPath(), ex.getMessage());
        }
    }

    private File createLogFile(UUID simulationId) {
        File logFile = new File("simulation_logs/%s.csv".formatted(simulationId));
        try {
            logFile.getParentFile().mkdirs();
            logFile.createNewFile();
        } catch (IOException ex) {
            System.out.printf("Failed to open a csv file: %s, with error: %s\n", logFile.getPath(), ex.getMessage());
        }
        return logFile;
    }

    private boolean isFirstLine(File logFile) {
        return logFile.length() == 0;
    }
}
