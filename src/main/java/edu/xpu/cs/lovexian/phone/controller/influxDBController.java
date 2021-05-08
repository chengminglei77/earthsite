package edu.xpu.cs.lovexian.phone.controller;

import edu.xpu.cs.lovexian.app.appadmin.utils.InfluxDbConnection;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class influxDBController {

    //需要使用的地方直接注入
    @Autowired
    InfluxDbConnection influxDBConnection;

    @Value("${spring.influx.database}")
    private String database;


    @GetMapping("/influxDBQuery")
    public Map<String, List<Object>> influxDBQuery() {

        String[] settingsId = {"SDGW01DTU0201000602", "SDGW01DTU0201000601"};

        Map<String, List<Object>> map = new HashMap<>();

        for (int i = 0; i < settingsId.length; i++) {
            LocalDateTime dateTime = LocalDateTime.now();
            String datetemp = dateTime.toString().substring(0, 10);
            System.out.println(settingsId[i]);
            QueryResult results = influxDBConnection
                    .query("SELECT * FROM sensor_data where time >= '" + datetemp + "T00:00:00Z' and sensor_id='"+settingsId[i]+"' order by time desc");
            //results.getResults()是同时查询多条SQL语句的返回值，此处我们只有一条SQL，所以只取第一个结果集即可。
            QueryResult.Result oneResult = results.getResults().get(0);
            List<Object> valueList = Collections.singletonList(oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0));
            map.put(settingsId[i], valueList);
        }
        return map;
    }

}














