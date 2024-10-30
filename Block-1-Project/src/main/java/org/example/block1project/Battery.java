package org.example.block1project;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;

import java.util.List;

public class Battery {

    public static void getBatteryInfo(VBox vbox) {
        // Initialize OSHI components
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        // Retrieve power sources
        List<PowerSource> powerSources = hal.getPowerSources();

        // Check if power sources are available
        if (powerSources.isEmpty()) {
            vbox.getChildren().add(new Label("No battery information available."));
            return;
        }

        // Display battery information for each power source
        for (PowerSource battery : powerSources) {
            Label name = new Label("Battery: " + battery.getName());
            Label remainingCapacity = new Label("Remaining Capacity: " + battery.getRemainingCapacityPercent() + "%");
            Label powerUsage = new Label("Power Usage (W): " + Math.abs(battery.getPowerUsageRate() / 1000));
            Label isCharging = new Label("Is Charging: " + battery.isCharging());
            Label voltage = new Label("Voltage (V): " + battery.getVoltage());

            // Add labels to VBox
            vbox.getChildren().addAll(name, remainingCapacity, powerUsage, isCharging, voltage);
        }
    }
}
