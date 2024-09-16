package cz.MarekKanok.solar_power_analyzer.service;


import cz.MarekKanok.solar_power_analyzer.model.SolarDataStatistics;
import cz.MarekKanok.solar_power_analyzer.util.ArgumentParser;
import cz.MarekKanok.solar_power_analyzer.util.CsvStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolarPowerProcessor {
    @Autowired
    private ISolarDataAggregationService solarDataAggregationService; // Použití rozhraní

    @Autowired
    private ISolarDataLoggingService solarDataLoggingService; // Použití rozhraní

    // Metoda pro spuštění zpracování
    public void processSolarData(String... args) {
        // Parsování argumentů
        ArgumentParser.ParsedArguments parsedArgs = ArgumentParser.parseArguments(args);
        if (parsedArgs == null) {
            return; // Chyba v argumentech
        }
        SolarDataStatistics stats = new SolarDataStatistics();

        // Načítání a zpracování dat z CSV pomocí nového CsvStreamReader
        CsvStreamReader csvReader = new CsvStreamReader();
        csvReader.readCsv("src/main/resources/dataexport.csv",
                solarDataAggregationService.processSolarData(parsedArgs.getFromDate(), parsedArgs.getToDate(), parsedArgs.getDayArg(), stats));

        // Výpis výsledků do konzole pomocí SolarDataLoggingService
        solarDataLoggingService.logResults(stats, parsedArgs.getDayArg());
    }
   /* @Autowired
    private SolarDataService solarDataService; // Závislost na SolarDataService

    // Metoda pro spuštění zpracování
    public void processSolarData(String... args) {
        // Parsování argumentů
        ArgumentParser.ParsedArguments parsedArgs = ArgumentParser.parseArguments(args);
        if (parsedArgs == null) {
            return; // Chyba v argumentech
        }
        SolarDataStatistics stats = new SolarDataStatistics();

        // Načítání a zpracování dat z CSV
        CSVReader csvReader = new CSVReader();
        csvReader.processCsv("src/main/resources/dataexport.csv",
                solarDataService.processSolarData(parsedArgs.getFromDate(), parsedArgs.getToDate(), parsedArgs.getDayArg(),stats));

        // Výpis výsledků do konzole
        solarDataService.logResults(stats, parsedArgs.getDayArg());
    }*/
}
