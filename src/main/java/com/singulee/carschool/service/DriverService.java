package com.singulee.carschool.service;



import com.singulee.carschool.pojo.Driver;
import com.singulee.carschool.pojo.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Service
public class DriverService {
    final private DriverMapper driverMapper;
    @Autowired
    public DriverService(DriverMapper driverMapper) {
        this.driverMapper = driverMapper;
    }

    /**
     * 获得驾驶证类型列表
     * @return
     */
    public List<Driver> getDriverList(){
        return driverMapper.selectDriverList();
    }
}
