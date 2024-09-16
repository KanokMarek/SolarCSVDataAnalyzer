package cz.MarekKanok.solar_power_analyzer.util;

import cz.MarekKanok.solar_power_analyzer.model.SolarData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class CsvStreamReader implements ICsvReader {
    private static final Logger logger = LoggerFactory.getLogger(CsvStreamReader.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

    @Override
    public void readCsv(String filePath, Consumer<SolarData> dataConsumer) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            boolean dataStarted = false;

            for (CSVRecord csvRecord : csvParser) {
                if (!dataStarted) {
                    if (csvRecord.get(0).equalsIgnoreCase("timestamp")) {
                        dataStarted = true;
                    }
                    continue;
                }

                try {
                    String dateTimeString = csvRecord.get(0);
                    String powerString = csvRecord.get(1);

                    if (powerString == null || powerString.isEmpty()) {
                        logger.warn("Skipping row with empty power value: {}", csvRecord.toString());
                        continue;
                    }

                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
                    double power = Double.parseDouble(powerString);

                    SolarData solarData = new SolarData(dateTime, power);
                    dataConsumer.accept(solarData);

                } catch (Exception e) {
                    logger.warn("Skipping invalid row: {}", csvRecord.toString(), e);
                }
            }

        } catch (IOException e) {
            logger.error("Error reading CSV file", e);
        }
    }
}
