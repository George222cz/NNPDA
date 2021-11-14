package cz.upce.project.dto;

import java.util.Date;

public class MeasurementDto {

    private Long id;

    private Double value;

    private Date timeOfMeasurement;

    private Long sensorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(Date timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
}
