package cz.upce.project.controller;

import cz.upce.project.dto.SensorDto;
import cz.upce.project.entity.Sensor;
import cz.upce.project.service.SensorServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@SecurityRequirement(name = "BasicAuth")
@RequestMapping(value = "/api/sensor")
public class SensorController {

    private final SensorServiceImpl sensorService;

    public SensorController(SensorServiceImpl sensorService) {
        this.sensorService = sensorService;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(){
         return ResponseEntity.badRequest().body("{\"message\":\"Cannot delete!\"}");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Sensor> getAllSensors(){
        return sensorService.getAllSensors();
    }

    @GetMapping("/device/{deviceId}")
    public List<Sensor> getSensorsByDeviceId(@PathVariable Long deviceId){
        return sensorService.getSensorsByDeviceId(deviceId);
    }

    @GetMapping("/user/{userId}")
    public List<Sensor> getAllSensorForUser(@PathVariable Long userId){
        return sensorService.getAllSensorForUser(userId);
    }

    @GetMapping("{sensorId}")
    public Sensor getSensorById(@PathVariable Long sensorId){
        return sensorService.getSensorById(sensorId);
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateSensor(@RequestBody SensorDto dto){
        return sensorService.createOrUpdateSensor(dto);
    }

    @DeleteMapping("{sensorId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Sensor> deleteSensor(@PathVariable Long sensorId){
        return sensorService.deleteSensor(sensorId);
    }

}
