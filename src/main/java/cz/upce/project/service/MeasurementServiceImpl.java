package cz.upce.project.service;

import cz.upce.project.dto.MeasurementDto;
import cz.upce.project.entity.Measurement;
import cz.upce.project.entity.Sensor;
import cz.upce.project.repository.MeasurementRepository;
import cz.upce.project.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MeasurementServiceImpl {

    private final MeasurementRepository measurementRepository;

    private final SensorRepository sensorRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public List<Measurement> createOrUpdateMeasurement(MeasurementDto dto) {
        Optional<Sensor> sensor = sensorRepository.findById(dto.getSensorId());
        if (sensor.isPresent()) {
            Measurement measurement = new Measurement();
            measurement.setSensor(sensor.get());
            measurement.setValue(dto.getValue());
            measurement.setTimeOfMeasurement(dto.getTimeOfMeasurement());
            measurementRepository.save(measurement);
            return measurementRepository.findAll();
        } else {
            throw new NoSuchElementException("Sensor with ID: " + dto.getSensorId() + " was not found!");
        }
    }

    public List<Measurement> deleteMeasurement(Long measurementId) {
        Optional<Measurement> byId = measurementRepository.findById(measurementId);
        if (byId.isPresent()) {
            measurementRepository.deleteById(measurementId);
            return measurementRepository.findAll();
        } else {
            throw new NoSuchElementException("Measurement with ID: " + measurementId + " was not found!");
        }
    }

    public void generateMeasurement() throws Exception {
        List<Sensor> allSensors = sensorRepository.findAll();
        if(!allSensors.isEmpty()){
            Measurement measurement = new Measurement();
            measurement.setSensor(allSensors.get(ThreadLocalRandom.current().nextInt(0, allSensors.size())));
            measurement.setValue(ThreadLocalRandom.current().nextDouble(0,50));
            measurementRepository.save(measurement);
            System.out.println("New measurement generated!");
            System.out.println("T:"+measurement.getTimeOfMeasurement()+"; V:"+measurement.getValue()+"; U:"+measurement.getSensor().getUnit()+";");
            System.out.println("S:"+measurement.getSensor().getSensorName()+"; ST:"+measurement.getSensor().getSensorType()+"; D:"+measurement.getSensor().getDevice().getDeviceName());
        }else{
            throw new Exception("There are no sensors!");
        }
    }

    public List<Measurement> getAllMeasurementsForSensor(Long sensorId) {
        return measurementRepository.findMeasurementsBySensor_Id(sensorId);
    }
}
