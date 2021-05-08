package edu.xpu.cs.lovexian.phone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.phone.entity.SensorData;
import edu.xpu.cs.lovexian.phone.mapper.PhoneSensorDataMapper;
import edu.xpu.cs.lovexian.phone.service.SensorDataService;
import org.springframework.stereotype.Service;

/**
 * (SensorData)表服务实现类
 *
 * @author makejava
 * @since 2021-05-06 11:36:51
 */
@Service
public class SensorDataServiceImpl extends ServiceImpl<PhoneSensorDataMapper, SensorData> implements SensorDataService {

}