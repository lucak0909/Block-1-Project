package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.HWDiskStore;

import java.util.List;

public class DiskReadWriteGraph {

    private final LineChart<Number, Number> readChart;
    private final LineChart<Number, Number> writeChart;
    private final XYChart.Series<Number, Number> readSeries;
    private final XYChart.Series<Number, Number> writeSeries;
    private final SystemInfo systemInfo;
    private final HWDiskStore disk;
    private int timeInMilliseconds = 0;

    private static final int MAX_TIME_RANGE = 10000; // 10 seconds in milliseconds
    private static final int UPDATE_INTERVAL = 100; // Update every 100ms
    private static final double TIME_SCALE = 0.7;

    private long previousBytesRead = 0;
    private long previousBytesWritten = 0;
    private long cumulativeReadSpeed = 0;
    private long cumulativeWriteSpeed = 0;
    private int sampleCount = 0;

    public DiskReadWriteGraph() {
        systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        List<HWDiskStore> diskStores = hardware.getDiskStores();
        disk = diskStores.isEmpty() ? null : diskStores.get(0);

        // Set up charts for Read and Write speeds with labeled axes and ticks
        readChart = createStyledChart("Disk Read Speed (KB/s)");
        writeChart = createStyledChart("Disk Write Speed (KB/s)");

        readSeries = new XYChart.Series<>();
        writeSeries = new XYChart.Series<>();

        readChart.getData().add(readSeries);
        writeChart.getData().add(writeSeries);

        startMonitoring();
    }

    private LineChart<Number, Number> createStyledChart(String title) {
        NumberAxis xAxis = new NumberAxis(0, MAX_TIME_RANGE / 1000, 1);
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Time (s)");
        yAxis.setLabel("Speed (KB/s)");

        xAxis.setTickUnit(1);
        yAxis.setAutoRanging(true);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
        chart.setStyle("-fx-background-color: transparent;");

        return chart;
    }

    private void startMonitoring() {
        if (disk == null) {
            System.err.println("No disk drive found.");
            return;
        }

        disk.updateAttributes();
        previousBytesRead = disk.getReadBytes();
        previousBytesWritten = disk.getWriteBytes();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> updateCharts()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateCharts() {
        disk.updateAttributes();

        long currentBytesRead = disk.getReadBytes();
        long currentBytesWritten = disk.getWriteBytes();

        long readSpeed = (currentBytesRead - previousBytesRead) / 1024;
        long writeSpeed = (currentBytesWritten - previousBytesWritten) / 1024;

        previousBytesRead = currentBytesRead;
        previousBytesWritten = currentBytesWritten;

        readSeries.getData().add(new XYChart.Data<>(timeInMilliseconds * TIME_SCALE / 1000.0, readSpeed));
        writeSeries.getData().add(new XYChart.Data<>(timeInMilliseconds * TIME_SCALE / 1000.0, writeSpeed));

        // Update cumulative read/write speeds and sample count for average calculation
        cumulativeReadSpeed += readSpeed;
        cumulativeWriteSpeed += writeSpeed;
        sampleCount++;

        // Calculate average speeds and update chart titles
        double averageReadSpeed = (double) cumulativeReadSpeed / sampleCount;
        double averageWriteSpeed = (double) cumulativeWriteSpeed / sampleCount;

        readChart.setTitle(String.format("Disk Read Speed (KB/s) - Avg: %.2f KB/s", averageReadSpeed));
        writeChart.setTitle(String.format("Disk Write Speed (KB/s) - Avg: %.2f KB/s", averageWriteSpeed));

        timeInMilliseconds += UPDATE_INTERVAL;

        if (timeInMilliseconds * TIME_SCALE > MAX_TIME_RANGE) {
            readSeries.getData().remove(0);
            writeSeries.getData().remove(0);
        }

        NumberAxis readXAxis = (NumberAxis) readChart.getXAxis();
        readXAxis.setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        readXAxis.setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);

        NumberAxis writeXAxis = (NumberAxis) writeChart.getXAxis();
        writeXAxis.setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        writeXAxis.setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);
    }

    public VBox getDiskReadWriteCharts() {
        VBox vbox = new VBox(10);  // 10px spacing between charts
        vbox.getChildren().addAll(readChart, writeChart);
        return vbox;
    }
}
