package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class CpuFrequencyChart {

    private XYChart.Series<Number, Number> frequencySeries;  // Series to hold CPU frequency data
    private int timeInMilliseconds = 0;  // Track elapsed time in milliseconds
    private static final int MAX_TIME_RANGE = 10000;  // Show the last 10 "graph seconds"
    private static final int UPDATE_INTERVAL = 100;   // Update interval (100ms)
    private static final double TIME_SCALE = 0.7;     // Faster time scale (70% of real-time speed)
    private CentralProcessor processor;
    private LineChart<Number, Number> frequencyChart;

    public CpuFrequencyChart() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        processor = hal.getProcessor();

        // Create the X and Y axes
        NumberAxis xAxis = new NumberAxis(0, MAX_TIME_RANGE / 1000, 1);  // Last 10 seconds (graph time)
        NumberAxis yAxis = new NumberAxis(0, 5, 0.5);  // Y-axis range between 0 and 5 GHz

        // Create a LineChart to display CPU frequency over time
        frequencyChart = new LineChart<>(xAxis, yAxis);
        frequencyChart.setTitle("CPU Max Frequency over time (GHz)");

        // Remove grid lines for a cleaner look
        frequencyChart.setHorizontalGridLinesVisible(false);
        frequencyChart.setVerticalGridLinesVisible(false);
        frequencyChart.setLegendVisible(false);

        // Create a Series to hold the data
        frequencySeries = new XYChart.Series<>();
        frequencySeries.setName("CPU Max Frequency (GHz)");

        // Add the series to the LineChart
        frequencyChart.getData().add(frequencySeries);
        frequencyChart.setCreateSymbols(false);  // Disable symbols on data points (just the line)

        // Start a Timeline to update the frequency regularly (every 0.1 seconds for smooth transitions)
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> updateCpuFrequency())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        timeline.play();  // Start the animation
    }

    // Method to update the CPU frequency and add data to the graph
    private void updateCpuFrequency() {
        // Get the max CPU frequency in GHz
        double maxFrequency = processor.getMaxFreq() / 1_000_000_000.0;  // Convert Hz to GHz
        //System.out.println("Max CPU Frequency: " + maxFrequency + " GHz");

        // Add the frequency to the series (with time on the X-axis, adjusted by TIME_SCALE)
        frequencySeries.getData().add(new XYChart.Data<>((timeInMilliseconds * TIME_SCALE / 1000.0), maxFrequency));

        // Increment the time counter
        timeInMilliseconds += UPDATE_INTERVAL;

        // Remove old data points to keep the X-axis range constant (last 10 seconds in graph time)
        if (timeInMilliseconds * TIME_SCALE > MAX_TIME_RANGE) {
            frequencySeries.getData().remove(0);  // Remove the oldest data point
        }

        // Update the X-axis range to keep showing the last 10 seconds (in graph time)
        ((NumberAxis) frequencySeries.getChart().getXAxis()).setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        ((NumberAxis) frequencySeries.getChart().getXAxis()).setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);
    }

    // Method to return the LineChart for embedding in the GUI
    public LineChart<Number, Number> getFrequencyChart() {
        return frequencyChart;
    }
}
