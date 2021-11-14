package cz.upce.project.controller;

import cz.upce.project.dto.MeasurementDto;
import cz.upce.project.entity.Measurement;
import cz.upce.project.service.MeasurementServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@SecurityRequirement(name = "BasicAuth")
@RequestMapping(value = "/api/measurement")
public class MeasurementController {

    private final MeasurementServiceImpl measurementService;

    public MeasurementController(MeasurementServiceImpl measurementService) {
        this.measurementService = measurementService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Measurement> getAllMeasurement(){
        return measurementService.getAllMeasurements();
    }

    @GetMapping("/sensor/{sensorId}")
    public List<Measurement> getAllMeasurementForSensor(@PathVariable Long sensorId){
        return measurementService.getAllMeasurementsForSensor(sensorId);
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    public List<Measurement> createOrUpdateMeasurement(@RequestBody MeasurementDto dto){
        return measurementService.createOrUpdateMeasurement(dto);
    }

    @DeleteMapping("{measurementId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Measurement> deleteMeasurement(@PathVariable Long measurementId){
        return measurementService.deleteMeasurement(measurementId);
    }
}
