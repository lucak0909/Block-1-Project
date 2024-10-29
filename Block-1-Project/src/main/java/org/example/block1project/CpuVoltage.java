package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.hardware.HardwareAbstractionLayer;

public class CpuVoltage {

    private XYChart.Series<Number, Number> voltageSeries;  // Series to hold
    private int timeInMilliseconds = 0;  // Track elapsed time in milliseconds
    private static final int MAX_TIME_RANGE = 10000;  // Show the last 10 "graph seconds"
    private static final int UPDATE_INTERVAL = 100;   // Update interval (100ms)
    private static final double TIME_SCALE = 0.7;     // Faster time scale (70% of real-time speed)
    private Sensors voltage;
    private LineChart<Number, Number> voltageChart;

    public CpuVoltage() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        voltage = hal.getSensors();

        // Create the X and Y axes
        NumberAxis xAxis = new NumberAxis(0, MAX_TIME_RANGE / 1000, 1);  // Last 10 seconds (graph time)
        NumberAxis yAxis = new NumberAxis(0, 100, 10);  // Y-axis range between 0 and 100 (percentage)

        // Create a LineChart to display CPU clock speed over time
        voltageChart = new LineChart<>(xAxis, yAxis);
        voltageChart.setTitle("CPU Voltage over time");

        // Remove grid lines for a cleaner look
        voltageChart.setHorizontalGridLinesVisible(false);
        voltageChart.setVerticalGridLinesVisible(false);
        voltageChart.setLegendVisible(false);

        // Create a Series to hold the data
        voltageSeries = new XYChart.Series<>();
        voltageSeries.setName("CPU Voltage");

        // Add the series to the LineChart
        voltageChart.getData().add(voltageSeries);
        voltageChart.setCreateSymbols(false);  // Disable symbols on data points (just the line)

        // Start a Timeline to update the clock speed regularly (every 0.1 seconds for smooth transitions)
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> updateCpuVoltage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        timeline.play();  // Start the animation
    }

    // Method to update the CPU clock speed and add data to the graph
    private void updateCpuVoltage() {


        double voltage1 = voltage.getCpuVoltage();

        // Add the clock speed to the series (with time on the X-axis, adjusted by TIME_SCALE)
        voltageSeries.getData().add(new XYChart.Data<>((timeInMilliseconds * TIME_SCALE / 1000.0), voltage1));

        // Increment the time counter
        timeInMilliseconds += UPDATE_INTERVAL;

        // Remove old data points to keep the X-axis range constant (last 10 seconds in graph time)
        if (timeInMilliseconds * TIME_SCALE > MAX_TIME_RANGE) {
            voltageSeries.getData().remove(0);  // Remove the oldest data point
        }

        // Update the X-axis range to keep showing the last 10 seconds (in graph time)
        ((NumberAxis) voltageSeries.getChart().getXAxis()).setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        ((NumberAxis) voltageSeries.getChart().getXAxis()).setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);
    }

    // Method to return the LineChart for embedding in the GUI
    public LineChart<Number, Number> getVoltageChart() {
        return voltageChart;
    }
}

