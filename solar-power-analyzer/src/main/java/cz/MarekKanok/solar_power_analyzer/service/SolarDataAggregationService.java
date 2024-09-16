package cz.MarekKanok.solar_power_analyzer.service;

import cz.MarekKanok.solar_power_analyzer.model.SolarData;
import cz.MarekKanok.solar_power_analyzer.model.SolarDataStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

@Service
public class SolarDataAggregationService implements ISolarDataAggregationService {
    private static final Logger logger = LoggerFactory.getLogger(SolarDataAggregationService.class);
    private static final String[] MONTH_NAMES = {
            "Leden", "Únor", "Březen", "Duben", "Květen", "Červen",
            "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"
    };

    @Override
    public Consumer<SolarData> processSolarData(LocalDate fromDate, LocalDate toDate, Integer dayOfWeek, SolarDataStatistics stats) {
        return solarData -> {
            LocalDateTime dateTime = solarData.getDateTime();
            LocalDate date = dateTime.toLocalDate();

            // Filtrování podle data
            if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                int year = dateTime.getYear();
                int monthIndex = dateTime.getMonthValue() - 1; // Index pro pole 0-11
                boolean isDayMatch = dayOfWeek == null || dateTime.getDayOfWeek().getValue() == dayOfWeek;

                // Pokud záznam odpovídá dni v týdnu a dayOfWeek je zadán, okamžitě ho vypiš do konzole
                if (isDayMatch && dayOfWeek != null) {
                    String dayOfWeekName = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));
                    logger.info("Výkon - {} - {}: {} W/m2", dayOfWeekName, date, solarData.getPower());
                }

                // Kontrola změny roku nebo měsíce
                if (stats.getCurrentYear() != -1 && (stats.getCurrentYear() != year || stats.getCurrentMonthIndex() != monthIndex)) {
                    // Pokud se změnil rok nebo měsíc, vypiš souhrny pro předchozí měsíc
                    SolarDataLoggingService.logMonthlySummary(stats, stats.getCurrentYear(), stats.getCurrentMonthIndex(), dayOfWeek);
                    // Vyčištění dat pro předchozí měsíc
                    stats.clearMonthData(stats.getCurrentYear(), stats.getCurrentMonthIndex());
                }

                // Agregace celkového výkonu a měsíčního výkonu
                stats.addPower(solarData.getPower(), year, monthIndex, solarData, isDayMatch);
            }
        };
    }
}
