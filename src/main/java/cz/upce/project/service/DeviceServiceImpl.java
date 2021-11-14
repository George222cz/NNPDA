package cz.upce.project.service;

import cz.upce.project.dto.DeviceDto;
import cz.upce.project.entity.Device;
import cz.upce.project.entity.Sensor;
import cz.upce.project.repository.DeviceRepository;
import cz.upce.project.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DeviceServiceImpl {
    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final SensorServiceImpl sensorService;

    public DeviceServiceImpl(DeviceRepository deviceRepository, UserRepository userRepository, SensorServiceImpl sensorService) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.sensorService = sensorService;
    }

    public List<Device> getAllDevices(){
        return deviceRepository.findAll();
    }

    public List<Device> createOrUpdateDevice(DeviceDto dto){
        Device device = new Device();
        device.setId(dto.getId());
        device.setDeviceName(dto.getDeviceName());
        device.setDescription(dto.getDescription());
        device.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        deviceRepository.save(device);
        return deviceRepository.findDevicesByUser_Id(dto.getUserId());
    }

    public List<Device> deleteDevice(Long deviceId){
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            for (Sensor sensor : device.get().getSensors()) {
                sensorService.deleteSensor(sensor.getId());
            }
            deviceRepository.deleteById(deviceId);
            return deviceRepository.findDevicesByUser_Id(device.get().getUser().getId());
        } else {
            throw new NoSuchElementException("Device with ID: " + deviceId + " was not found!");
        }
    }

    public List<Device> getAllDevicesForUser(Long userId) {
        return deviceRepository.findDevicesByUser_Id(userId);
    }
}
