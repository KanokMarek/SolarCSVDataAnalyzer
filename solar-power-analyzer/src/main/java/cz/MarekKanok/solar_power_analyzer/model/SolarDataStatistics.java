package cz.MarekKanok.solar_power_analyzer.model;

import java.util.HashMap;
import java.util.Map;

public class SolarDataStatistics {
    private double totalPower;
    private Map<Integer, Map<Integer, Double>> yearlyMonthlyPower; // Mapa pro každý rok a měsíc
    private Map<Integer, Map<Integer, Double>> yearlyMonthlyDaySums; // Součet výkonu pro každý měsíc v roce
    private Map<Integer, Map<Integer, Integer>> yearlyMonthlyDayCounts; // Počet záznamů pro každý měsíc v roce

    private int currentYear = -1; // Aktuální rok
    private int currentMonthIndex = -1; // Aktuální měsíc

    public SolarDataStatistics() {
        this.totalPower = 0;
        this.yearlyMonthlyPower = new HashMap<>();
        this.yearlyMonthlyDaySums = new HashMap<>();
        this.yearlyMonthlyDayCounts = new HashMap<>();
    }

    public double getTotalPower() {
        return totalPower;
    }

    public Map<Integer, Map<Integer, Double>> getYearlyMonthlyPower() {
        return yearlyMonthlyPower;
    }

    public Map<Integer, Map<Integer, Double>> getYearlyMonthlyDaySums() {
        return yearlyMonthlyDaySums;
    }

    public Map<Integer, Map<Integer, Integer>> getYearlyMonthlyDayCounts() {
        return yearlyMonthlyDayCounts;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonthIndex() {
        return currentMonthIndex;
    }

    public void setCurrentYear(int year) {
        this.currentYear = year;
    }

    public void setCurrentMonthIndex(int monthIndex) {
        this.currentMonthIndex = monthIndex;
    }

    public void addPower(double power, int year, int monthIndex, SolarData solarData, boolean trackDay) {
        this.totalPower += power;

        // Inicializace vnořených map pro rok a měsíc, pokud ještě neexistují
        yearlyMonthlyPower.computeIfAbsent(year, k -> new HashMap<>())
                .merge(monthIndex, power, Double::sum);

        if (trackDay) {
            // Agregujeme součet výkonu a počet záznamů pro průměr
            yearlyMonthlyDaySums.computeIfAbsent(year, k -> new HashMap<>())
                    .merge(monthIndex, power, Double::sum);
            yearlyMonthlyDayCounts.computeIfAbsent(year, k -> new HashMap<>())
                    .merge(monthIndex, 1, Integer::sum);
        }

        // Nastavíme aktuální rok a měsíc
        setCurrentYear(year);
        setCurrentMonthIndex(monthIndex);
    }

    // Metoda pro vyčištění uložených dat pro konkrétní měsíc v roce
    public void clearMonthData(int year, int monthIndex) {
        Map<Integer, Double> monthlyPower = yearlyMonthlyPower.get(year);
        if (monthlyPower != null) {
            monthlyPower.remove(monthIndex);
        }

        Map<Integer, Double> monthlyDaySums = yearlyMonthlyDaySums.get(year);
        if (monthlyDaySums != null) {
            monthlyDaySums.remove(monthIndex);
        }

        Map<Integer, Integer> monthlyDayCounts = yearlyMonthlyDayCounts.get(year);
        if (monthlyDayCounts != null) {
            monthlyDayCounts.remove(monthIndex);
        }
    }
}
