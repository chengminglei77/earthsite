package edu.xpu.cs.lovexian.phone.controller;

import edu.xpu.cs.lovexian.app.appadmin.utils.InfluxDbConnection;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class influxDBController {

    //需要使用的地方直接注入
    @Autowired
    InfluxDbConnection influxDBConnection;

    @Value("${spring.influx.database}")
    private String database;

    @GetMapping("/influxDBQuery")
    public EarthSiteResponse influxDBQuery() {
        LocalDateTime dateTime=LocalDateTime.now();
        String datetemp=dateTime.toString().substring(0,10);
        QueryResult results = influxDBConnection
                .query("SELECT * FROM sensor_data where time >= '"+datetemp+"T00:00:00Z' order by time desc");
        //results.getResults()是同时查询多条SQL语句的返回值，此处我们只有一条SQL，所以只取第一个结果集即可。
        QueryResult.Result oneResult = results.getResults().get(0);

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues)
                    .collect(Collectors.toList()).get(0);

          return EarthSiteResponse.SUCCESS().data(valueList);
        }
        return EarthSiteResponse.FAIL().message("无数据");
    }
}

