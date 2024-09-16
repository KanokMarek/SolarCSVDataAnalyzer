package cz.MarekKanok.solar_power_analyzer.model;

import java.time.LocalDateTime;

public class SolarData {
    private LocalDateTime dateTime;
    private double power;

    public SolarData(LocalDateTime dateTime, double power) {
        this.dateTime = dateTime;
        this.power = power;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "SolarData{" +
                "dateTime=" + dateTime +
                ", power=" + power +
                '}';
    }
}
