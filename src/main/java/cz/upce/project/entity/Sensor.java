package cz.upce.project.entity;

import com.fasterxml.jackson.annotation.*;
import cz.upce.project.dto.SensorType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;


@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String sensorName;

    @Column
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SensorType sensorType = SensorType.OTHER;

    @ManyToOne
    private Device device;

    @OneToMany(mappedBy = "sensor")
    @JsonIgnore
    private Set<Measurement> measurements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(id, sensor.id) &&
                Objects.equals(sensorName, sensor.sensorName) &&
                Objects.equals(unit, sensor.unit) &&
                Objects.equals(sensorType, sensor.sensorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sensorName, unit, sensorType);
    }
}
