package edu.xpu.cs.lovexian.phone.service;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.phone.mapper.SensorInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SensorInfoService {
    @Autowired
    SensorInfoMapper sensorInfoMapper;

    public List<AdminCollectData> findDeviceId() {
        return sensorInfoMapper.findDeviceId();
    }

    public List<AdminCollectData> getSensorInfo(){
        return sensorInfoMapper.getSensorInfo();
    }
}
