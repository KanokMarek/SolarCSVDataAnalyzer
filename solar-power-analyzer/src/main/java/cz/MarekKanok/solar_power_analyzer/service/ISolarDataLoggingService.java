package cz.MarekKanok.solar_power_analyzer.service;

import cz.MarekKanok.solar_power_analyzer.model.SolarDataStatistics;

public interface ISolarDataLoggingService {
    void logResults(SolarDataStatistics stats, Integer dayOfWeek);
}
