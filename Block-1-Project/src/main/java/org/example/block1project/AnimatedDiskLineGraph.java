package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

import java.util.List;

public class AnimatedDiskLineGraph {

    private XYChart.Series<Number, Number> readSeries;
    private XYChart.Series<Number, Number> writeSeries;
    private int timeInMilliseconds = 0;
    private static final int MAX_TIME_RANGE = 10000;  // 10 seconds range
    private static final int UPDATE_INTERVAL = 100;   // Update every 100 ms
    private static final double TIME_SCALE = 0.7;
    private LineChart<Number, Number> lineChart;

    // OSHI objects for disk monitoring
    private SystemInfo systemInfo;
    private HWDiskStore disk;

    public AnimatedDiskLineGraph() {
        // Initialize OSHI SystemInfo and select first available disk
        systemInfo = new SystemInfo();
        List<HWDiskStore> disks = systemInfo.getHardware().getDiskStores();
        if (disks.isEmpty()) {
            throw new IllegalStateException("No disks available to monitor");
        }
        disk = disks.get(0);

        // Create X and Y axes
        NumberAxis xAxis = new NumberAxis(0, MAX_TIME_RANGE / 1000, 1);
        NumberAxis yAxis = new NumberAxis(0, 100, 10);  // Adjust the max based on expected speeds
        xAxis.setLabel("Time (s)");
        yAxis.setLabel("Speed (MB/s)");

        // Create LineChart for displaying disk read/write rates
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Disk Read/Write Speeds (MB/s)");
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setLegendVisible(true);

        // Set up two Series for read and write speeds
        readSeries = new XYChart.Series<>();
        readSeries.setName("Read Speed");
        writeSeries = new XYChart.Series<>();
        writeSeries.setName("Write Speed");

        lineChart.getData().addAll(readSeries, writeSeries);
        lineChart.setCreateSymbols(false);
        lineChart.setStyle("-fx-background-color: transparent;");

        // Start Timeline to update disk speeds regularly
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> updateDiskSpeeds())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Method to update disk read/write speeds and add data to the graph
    private void updateDiskSpeeds() {
        disk.updateAttributes();

        // Read and Write speeds in MB/s
        double readSpeed = (disk.getReadBytes() / (1024.0 * 1024.0)) / (UPDATE_INTERVAL / 1000.0);
        double writeSpeed = (disk.getWriteBytes() / (1024.0 * 1024.0)) / (UPDATE_INTERVAL / 1000.0);

        // Update series with new data points
        readSeries.getData().add(new XYChart.Data<>(timeInMilliseconds * TIME_SCALE / 1000.0, readSpeed));
        writeSeries.getData().add(new XYChart.Data<>(timeInMilliseconds * TIME_SCALE / 1000.0, writeSpeed));
        timeInMilliseconds += UPDATE_INTERVAL;

        // Keep the graph within the MAX_TIME_RANGE
        if (timeInMilliseconds * TIME_SCALE > MAX_TIME_RANGE) {
            readSeries.getData().remove(0);
            writeSeries.getData().remove(0);
        }

        // Update X-axis range to keep it scrolling
        NumberAxis xAxis = (NumberAxis) readSeries.getChart().getXAxis();
        xAxis.setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        xAxis.setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);
    }

    // Method to return the LineChart for embedding in other GUIs
    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }
}
