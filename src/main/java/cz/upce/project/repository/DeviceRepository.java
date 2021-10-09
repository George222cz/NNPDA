package cz.upce.project.repository;

import cz.upce.project.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    Device findDeviceByDeviceNameContains(String contains);
}
