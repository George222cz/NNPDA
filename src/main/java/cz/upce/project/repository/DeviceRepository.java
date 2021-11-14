package cz.upce.project.repository;

import cz.upce.project.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    List<Device> findDevicesByUser_Id(Long userId);
}
