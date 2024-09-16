package cz.MarekKanok.solar_power_analyzer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ArgumentParser {
    private static final Logger logger = LoggerFactory.getLogger(ArgumentParser.class);
    private static final DateTimeFormatter ARGUMENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static ParsedArguments parseArguments(String... args) {
        if (args.length < 2) {
            logger.error("Nedostatečný počet argumentů. Použití: java -jar solar-power-analyzer.jar <FROM_YYYYMM> <TO_YYYYMM> [<DAY>]");
            return null;
        }

        String fromArg = args[0];
        String toArg = args[1];
        Integer dayArg = null;
        boolean byYear = false;


        // Validace dayArg
        if (args.length >= 3) {
            try {
                dayArg = Integer.parseInt(args[2]);
                if (dayArg < 1 || dayArg > 7) {
                    logger.error("Argument <DAY> musí být v rozmezí 1-7.");
                    return null;
                }
            } catch (NumberFormatException e) {
                logger.error("Argument <DAY> musí být celé číslo v rozmezí 1-7.");
                return null;
            }
        }

        // Parsování datových argumentů
        LocalDate fromDate;
        LocalDate toDate;
        try {
            YearMonth fromYearMonth = YearMonth.parse(fromArg, ARGUMENT_DATE_FORMATTER);
            YearMonth toYearMonth = YearMonth.parse(toArg, ARGUMENT_DATE_FORMATTER);

            // Nastavíme fromDate na první den měsíce
            fromDate = fromYearMonth.atDay(1);

            // Nastavíme toDate na poslední den měsíce
            toDate = toYearMonth.atEndOfMonth();
        } catch (Exception e) {
            logger.error("Argumenty <FROM_YYYYMM> a <TO_YYYYMM> musí být ve formátu YYYYMM.");
            return null;
        }

        logger.info("Filtrujeme data od {} do {}", fromDate, toDate);
        if (dayArg != null) {
            logger.info("Filtrujeme data pro dny týdne: {}", dayArg);
        }

        return new ParsedArguments(fromDate, toDate, dayArg);
    }

    // Pomocná třída pro uchování parsovaných argumentů
    public static class ParsedArguments {
        private final LocalDate fromDate;
        private final LocalDate toDate;
        private final Integer dayArg;

        public ParsedArguments(LocalDate fromDate, LocalDate toDate, Integer dayArg) {
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.dayArg = dayArg;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public Integer getDayArg() {
            return dayArg;
        }
    }
}
