package com.sari.sourceapi.service;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sari.sourceapi.model.AreaInfo;
import com.sari.sourceapi.model.MachineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvService {
    private static final Logger log = LoggerFactory.getLogger(CsvService.class);
    private List<MachineData> machineDataList;

    /**
     * Ensures the CSV data is loaded into machineDataList.
     */
    private void ensureDataIsLoaded() {
        if (machineDataList == null) {
            machineDataList = readCsv();
        }
    }

    public List<MachineData> readCsv() {
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("ai4i2020.csv"))) {

            CsvToBean<MachineData> csvToBean = new CsvToBeanBuilder<MachineData>(reader)
                    .withType(MachineData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false) // Ignore errors on mismatched rows
                    .build();

            List<MachineData> listMachines = csvToBean.parse();

            // Randomly assign Floor and Area
            List<AreaInfo> areas = getAreas(); // Define your areas here
            Random random = new Random();

            for (MachineData machine : listMachines) {
                AreaInfo randomArea = areas.get(random.nextInt(areas.size()));
                machine.setFloor(String.valueOf(randomArea.getFloor()));
                machine.setArea(String.valueOf(randomArea.getId()));
            }

            return listMachines;
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
    /**
     * Returns aggregated statistics from the machine data.
     */
    public Map<String, Object> getStatistics() {
        ensureDataIsLoaded(); // Ensure data is loaded

        Map<String, Object> stats = new HashMap<>();

        // Example aggregations
        stats.put("totalMachines", machineDataList.size());
        stats.put("averageAirTemperature", calculateAverage("airTemperature"));
        stats.put("averageProcessTemperature", calculateAverage("processTemperature"));
        stats.put("failureCounts", calculateFailureCounts());

        return stats;
    }

    /**
     * Calculate average for a specific field.
     */
    private double calculateAverage(String fieldName) {
        return machineDataList.stream()
                .mapToDouble(machine -> {
                    switch (fieldName) {
                        case "airTemperature":
                            return Double.parseDouble(machine.getAirTemperature());
                        case "processTemperature":
                            return Double.parseDouble(machine.getProcessTemperature());
                        default:
                            return 0.0;
                    }
                })
                .average()
                .orElse(0.0);
    }

    /**
     * Calculate failure counts for each type of failure.
     */
    private Map<String, Long> calculateFailureCounts() {
        Map<String, Long> failureCounts = new HashMap<>();
        failureCounts.put("TWF", machineDataList.stream().filter(m -> "1".equals(m.getTWF())).count());
        failureCounts.put("HDF", machineDataList.stream().filter(m -> "1".equals(m.getHDF())).count());
        failureCounts.put("PWF", machineDataList.stream().filter(m -> "1".equals(m.getPWF())).count());
        failureCounts.put("OSF", machineDataList.stream().filter(m -> "1".equals(m.getOSF())).count());
        failureCounts.put("RNF", machineDataList.stream().filter(m -> "1".equals(m.getRNF())).count());
        return failureCounts;
    }

    /**
     * Get the maximum rotational speed.
     */
    private double getMaxRotationalSpeed() {
        return machineDataList.stream()
                .mapToDouble(machine -> Double.parseDouble(machine.getRotationalSpeed()))
                .max()
                .orElse(0.0);
    }

    /**
     * Get the minimum torque.
     */
    private double getMinTorque() {
        return machineDataList.stream()
                .mapToDouble(machine -> Double.parseDouble(machine.getTorque()))
                .min()
                .orElse(0.0);
    }

    /**
     * Calculate the percentage of machines with failures.
     */
    private double calculateFailurePercentage() {
        long failureCount = machineDataList.stream()
                .filter(machine -> Arrays.asList(
                        machine.getTWF(),
                        machine.getHDF(),
                        machine.getPWF(),
                        machine.getOSF(),
                        machine.getRNF()
                ).contains("1"))
                .count();
        return ((double) failureCount / machineDataList.size()) * 100;
    }

    public Map<String, Long> getTypeDistribution() {
        ensureDataIsLoaded();

        // Group machines by type and count each type
        return machineDataList.stream()
                .collect(Collectors.groupingBy(MachineData::getType, Collectors.counting()));
    }

    public Map<String, Long> getAirTemperatureDistribution() {
        ensureDataIsLoaded();

        // Define temperature ranges
        List<String> ranges = List.of(
                "200-250", "251-300", "301-350", "351-400", "401-450"
        );

        // Initialize the distribution map
        Map<String, Long> distribution = ranges.stream()
                .collect(Collectors.toMap(range -> range, range -> 0L));

        // Count machines in each range
        for (MachineData machine : machineDataList) {
            try {
                double airTemp = Double.parseDouble(machine.getAirTemperature());
                if (airTemp >= 200 && airTemp <= 250) distribution.put("200-250", distribution.get("200-250") + 1);
                else if (airTemp >= 251 && airTemp <= 300) distribution.put("251-300", distribution.get("251-300") + 1);
                else if (airTemp >= 301 && airTemp <= 350) distribution.put("301-350", distribution.get("301-350") + 1);
                else if (airTemp >= 351 && airTemp <= 400) distribution.put("351-400", distribution.get("351-400") + 1);
                else if (airTemp >= 401 && airTemp <= 450) distribution.put("401-450", distribution.get("401-450") + 1);
            } catch (NumberFormatException e) {
                System.err.println("Invalid air temperature value: " + machine.getAirTemperature());
            }
        }

        return distribution;
    }

    public Map<String, Double> getAggregatedTorqueVsSpeedData() {
        ensureDataIsLoaded();

        // Define speed ranges
        List<String> ranges = List.of(
                "0-500", "501-1000", "1001-1500", "1501-2000", "2001-2500", "2501-3000"
        );

        // Initialize aggregation map
        Map<String, List<Double>> rangeTorqueMap = ranges.stream()
                .collect(Collectors.toMap(range -> range, range -> new ArrayList<>()));

        // Assign machines to speed ranges and collect torque values
        for (MachineData machine : machineDataList) {
            try {
                double speed = Double.parseDouble(machine.getRotationalSpeed());
                double torque = Double.parseDouble(machine.getTorque());

                if (speed >= 0 && speed <= 500) rangeTorqueMap.get("0-500").add(torque);
                else if (speed >= 501 && speed <= 1000) rangeTorqueMap.get("501-1000").add(torque);
                else if (speed >= 1001 && speed <= 1500) rangeTorqueMap.get("1001-1500").add(torque);
                else if (speed >= 1501 && speed <= 2000) rangeTorqueMap.get("1501-2000").add(torque);
                else if (speed >= 2001 && speed <= 2500) rangeTorqueMap.get("2001-2500").add(torque);
                else if (speed >= 2501 && speed <= 3000) rangeTorqueMap.get("2501-3000").add(torque);
            } catch (NumberFormatException e) {
                System.err.println("Invalid data for machine: " + machine.getUDI());
            }
        }

        // Calculate average torque for each range
        return rangeTorqueMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0)
                ));
    }

    public List<Map<String, Object>> getMachinePerformanceOverview() {
        ensureDataIsLoaded();

        // Group data by machine type
        Map<String, List<MachineData>> machinesByType = machineDataList.stream()
                .collect(Collectors.groupingBy(MachineData::getType));

        // Calculate metrics for each machine type
        List<Map<String, Object>> performanceOverview = new ArrayList<>();
        for (Map.Entry<String, List<MachineData>> entry : machinesByType.entrySet()) {
            String type = entry.getKey();
            List<MachineData> machines = entry.getValue();

            // Calculate average torque
            double averageTorque = machines.stream()
                    .mapToDouble(machine -> Double.parseDouble(machine.getTorque()))
                    .average()
                    .orElse(0.0);

            // Calculate failure percentage
            long totalMachines = machines.size();
            long failedMachines = machines.stream()
                    .filter(machine -> Arrays.asList(
                            machine.getTWF(),
                            machine.getHDF(),
                            machine.getPWF(),
                            machine.getOSF(),
                            machine.getRNF()
                    ).contains("1"))
                    .count();
            double failurePercentage = ((double) failedMachines / totalMachines) * 100;

            // Calculate average air temperature
            double averageAirTemperature = machines.stream()
                    .mapToDouble(machine -> Double.parseDouble(machine.getAirTemperature()))
                    .average()
                    .orElse(0.0);

            // Add data for this type
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("type", type);
            typeData.put("averageTorque", averageTorque);
            typeData.put("failurePercentage", failurePercentage);
            typeData.put("averageAirTemperature", averageAirTemperature);

            performanceOverview.add(typeData);
        }

        return performanceOverview;
    }

    private List<AreaInfo> getAreas() {
        return Arrays.asList(
                new AreaInfo(1, "Main Production Floor", 1),
                new AreaInfo(2, "Quality Control Lab", 1),
                new AreaInfo(3, "Conference Room A", 2),
                new AreaInfo(4, "Executive Offices", 2),
                new AreaInfo(5, "Break Room", 1),
                new AreaInfo(6, "Maintenance Workshop", 1),
                new AreaInfo(7, "Restrooms", 1),
                new AreaInfo(8, "R&D Lab", 2)
        );
    }
}
