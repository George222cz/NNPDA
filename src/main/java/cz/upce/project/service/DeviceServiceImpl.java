package cz.upce.project.service;

import cz.upce.project.dto.DeviceDto;
import cz.upce.project.entity.Device;
import cz.upce.project.repository.DeviceRepository;
import cz.upce.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DeviceServiceImpl {
    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
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
        return deviceRepository.findAll();
    }

    public List<Device> deleteDevice(Long deviceId){
        Optional<Device> byId = deviceRepository.findById(deviceId);
        if (byId.isPresent()) {
            deviceRepository.deleteById(deviceId);
            return deviceRepository.findAll();
        } else {
            throw new NoSuchElementException("Device with ID: " + deviceId + " was not found!");
        }
    }
}
