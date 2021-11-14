package cz.upce.project.repository;

import cz.upce.project.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement,Long> {

    List<Measurement> findMeasurementsBySensor_Id(Long sensorId);
}
