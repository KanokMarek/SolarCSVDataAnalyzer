package cz.MarekKanok.solar_power_analyzer.util;

import cz.MarekKanok.solar_power_analyzer.model.SolarData;

import java.util.function.Consumer;

public interface ICsvReader {
    void readCsv(String filePath, Consumer<SolarData> dataConsumer);
}
