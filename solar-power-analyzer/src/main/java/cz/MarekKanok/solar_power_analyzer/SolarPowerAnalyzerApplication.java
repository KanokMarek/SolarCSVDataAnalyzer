package cz.MarekKanok.solar_power_analyzer;

import cz.MarekKanok.solar_power_analyzer.service.SolarPowerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolarPowerAnalyzerApplication implements CommandLineRunner {

    @Autowired
    private SolarPowerProcessor solarPowerProcessor;

    public static void main(String[] args) {
        SpringApplication.run(SolarPowerAnalyzerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        solarPowerProcessor.processSolarData(args);
    }


}
