package cz.upce.project.service;

import cz.upce.project.dto.SensorDto;
import cz.upce.project.dto.SensorType;
import cz.upce.project.entity.Sensor;
import cz.upce.project.entity.Device;
import cz.upce.project.repository.MeasurementRepository;
import cz.upce.project.repository.SensorRepository;
import cz.upce.project.repository.DeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SensorServiceImpl {
    private final SensorRepository sensorRepository;

    private final DeviceRepository deviceRepository;

    private final MeasurementRepository measurementRepository;

    public SensorServiceImpl(SensorRepository sensorRepository, DeviceRepository deviceRepository, MeasurementRepository measurementRepository) {
        this.sensorRepository = sensorRepository;
        this.deviceRepository = deviceRepository;
        this.measurementRepository = measurementRepository;
    }

    public List<Sensor> getAllSensors(){
        return sensorRepository.findAll();
    }

    public List<Sensor> getSensorsByDeviceId(Long deviceId){
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            return sensorRepository.findSensorsByDeviceId(deviceId);
        } else {
            throw new NoSuchElementException("Device with ID: " + deviceId + " was not found!");
        }
    }

    public Sensor getSensorById(Long sensorId){
        if (sensorRepository.findById(sensorId).isPresent()) {
            return sensorRepository.findById(sensorId).get();
        } else {
            throw new NoSuchElementException("Sensor with ID: " + sensorId + " was not found!");
        }
    }

    public ResponseEntity<Object> createOrUpdateSensor(SensorDto dto){
        Optional<Device> device = deviceRepository.findById(dto.getDeviceId());
        if (device.isPresent()) {
            Sensor sensor = new Sensor();
            sensor.setId(dto.getId());
            sensor.setSensorName(dto.getSensorName());
            sensor.setUnit(dto.getUnit());
            sensor.setSensorType(SensorType.valueOf(dto.getSensorType()));
            sensor.setDevice(device.get());
            sensorRepository.save(sensor);
            return ResponseEntity.ok().build();
        } else {
            throw new NoSuchElementException("Device with ID: " + dto.getDeviceId() + " was not found!");
        }
    }

    public List<Sensor> deleteSensor(Long sensorId){
        Optional<Sensor> sensor = sensorRepository.findById(sensorId);
        if (sensor.isPresent()) {
            measurementRepository.deleteAll(sensor.get().getMeasurements());
            sensorRepository.deleteById(sensorId);
            return sensorRepository.findSensorsByDevice_User_Id(sensor.get().getDevice().getUser().getId());
        } else {
            throw new NoSuchElementException("Sensor with ID: " + sensorId + " was not found!");
        }
    }

    public List<Sensor> getAllSensorForUser(Long userId) {
        return sensorRepository.findSensorsByDevice_User_Id(userId);
    }
}
