package cz.upce.project.repository;

import cz.upce.project.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor,Long> {

    Sensor findSensorBySensorNameContains(String contains);

    List<Sensor> findSensorsByDeviceId(Long deviceId);
}
