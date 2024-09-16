package cz.MarekKanok.solar_power_analyzer.service;

import cz.MarekKanok.solar_power_analyzer.model.SolarData;
import cz.MarekKanok.solar_power_analyzer.model.SolarDataStatistics;

import java.time.LocalDate;
import java.util.function.Consumer;

public interface ISolarDataAggregationService {
    Consumer<SolarData> processSolarData(LocalDate fromDate, LocalDate toDate, Integer dayOfWeek, SolarDataStatistics stats);
}
