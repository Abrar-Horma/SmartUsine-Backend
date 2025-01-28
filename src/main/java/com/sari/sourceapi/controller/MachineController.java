package com.sari.sourceapi.controller;

import com.sari.sourceapi.model.MachineData;
import com.sari.sourceapi.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins ="*")
public class MachineController {

    @Autowired
    private CsvService csvService;

    @GetMapping("/machines")
    public List<MachineData> getMachines() {
        return csvService.readCsv();
    }

    @GetMapping("/machines/statistics")
    public Map<String, Object> getStatistics() {
        return csvService.getStatistics();
    }

    /**
     * Fetch graph data for distribution of machines by type.
     */
    @GetMapping("/machines/type-distribution")
    public Map<String, Long> getTypeDistributionGraph() {
        return csvService.getTypeDistribution();
    }

    @GetMapping("/machines/air-temperature-distribution")
    public Map<String, Long> getAirTemperatureDistribution() {
        return csvService.getAirTemperatureDistribution();
    }

    @GetMapping("/machines/torque-vs-speed")
    public Map<String, Double> getTorqueVsSpeedGraphData() {
        return csvService.getAggregatedTorqueVsSpeedData();
    }

    @GetMapping("/machines/performance-overview")
    public List<Map<String, Object>> getMachinePerformanceOverview() {
        return csvService.getMachinePerformanceOverview();
    }
}
