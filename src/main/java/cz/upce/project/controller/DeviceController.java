package cz.upce.project.controller;

import cz.upce.project.dto.DeviceDto;
import cz.upce.project.entity.Device;
import cz.upce.project.service.DeviceServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@SecurityRequirement(name = "BasicAuth")
@RequestMapping(value = "/api/device")
public class DeviceController {

    private final DeviceServiceImpl deviceService;

    public DeviceController(DeviceServiceImpl deviceService) {
        this.deviceService = deviceService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Device> getAllDevices(){
        return deviceService.getAllDevices();
    }

    @GetMapping("{userId}")
    public List<Device> getAllDevicesForUser(@PathVariable Long userId){
        return deviceService.getAllDevicesForUser(userId);
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Device> createOrUpdateDevice(@RequestBody DeviceDto dto){
        return deviceService.createOrUpdateDevice(dto);
    }

    @DeleteMapping("{deviceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Device> deleteDevice(@PathVariable Long deviceId){
        return deviceService.deleteDevice(deviceId);
    }

}
