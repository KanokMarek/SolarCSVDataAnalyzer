package cz.MarekKanok.solar_power_analyzer.service;

import cz.MarekKanok.solar_power_analyzer.model.SolarDataStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SolarDataLoggingService implements ISolarDataLoggingService {
    private static final Logger logger = LoggerFactory.getLogger(SolarDataLoggingService.class);
    private static final String[] MONTH_NAMES = {
            "Leden", "Únor", "Březen", "Duben", "Květen", "Červen",
            "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"
    };

    @Override
    public void logResults(SolarDataStatistics stats, Integer dayOfWeek) {
        // Pokud není zadaný třetí argument (dayOfWeek je null), vypisujeme jen celkový a měsíční výkon
        if (dayOfWeek == null) {
            // Iterujeme přes roky a měsíce
            for (Map.Entry<Integer, Map<Integer, Double>> yearEntry : stats.getYearlyMonthlyPower().entrySet()) {
                int year = yearEntry.getKey();
                Map<Integer, Double> monthlyPowerMap = yearEntry.getValue();

                for (Map.Entry<Integer, Double> monthEntry : monthlyPowerMap.entrySet()) {
                    int monthIndex = monthEntry.getKey();
                    double power = monthEntry.getValue();
                    if (power > 0) {
                        String monthName = MONTH_NAMES[monthIndex];
                        logger.info("Výkon pro měsíc {} {}: {} W/m2", monthName, year, power);
                    }
                }
            }

            // Výpis celkového výkonu pro zvolené období
            logger.info("Celkový výkon pro zvolené období: {} W/m2", stats.getTotalPower());
        } else {
            // Výpis souhrnů pro poslední měsíc (pokud existuje)
            if (stats.getCurrentMonthIndex() != -1 && stats.getCurrentYear() != -1) {
                logMonthlySummary(stats, stats.getCurrentYear(), stats.getCurrentMonthIndex(), dayOfWeek);
                stats.clearMonthData(stats.getCurrentYear(), stats.getCurrentMonthIndex());
            }

            // Výpis celkového výkonu pro zvolené období na závěr
            logger.info("Celkový výkon pro zvolené období: {} W/m2", stats.getTotalPower());
        }
    }

    public static void logMonthlySummary(SolarDataStatistics stats, int year, int monthIndex, Integer dayOfWeek) {
        String monthName = MONTH_NAMES[monthIndex];

        // Výpis celkového výkonu pro měsíc
        Double monthlyPower = stats.getYearlyMonthlyPower().getOrDefault(year, new HashMap<>()).get(monthIndex);
        if (monthlyPower != null) {
            logger.info("Výkon pro {} {}: {} W/m2", monthName, year, monthlyPower);
        }

        // Pokud je zadaný dayOfWeek, vypočítáme a vypíšeme průměrnou hodnotu
        if (dayOfWeek != null) {
            Double sum = stats.getYearlyMonthlyDaySums().getOrDefault(year, new HashMap<>()).getOrDefault(monthIndex, 0.0);
            Integer count = stats.getYearlyMonthlyDayCounts().getOrDefault(year, new HashMap<>()).getOrDefault(monthIndex, 0);
            double average = count > 0 ? sum / count : 0;
            logger.info("Průměrná hodnota pro zvolený den v {} {}: {} W/m2", monthName, year, average);
        }
    }
}
