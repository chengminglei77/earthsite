package edu.xpu.cs.lovexian.phone.service;

import edu.xpu.cs.lovexian.phone.entity.SensorData;
import edu.xpu.cs.lovexian.phone.mapper.SensorInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorInfoService {
    @Autowired
    SensorInfoMapper sensorInfoMapper;

    public List<SensorData> findDeviceId() {
        return sensorInfoMapper.findDeviceId();
    }

    public List<SensorData> getSensorInfo(){
        return sensorInfoMapper.getSensorInfo();
    }
}
