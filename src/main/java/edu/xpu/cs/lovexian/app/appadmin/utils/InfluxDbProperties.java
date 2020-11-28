package edu.xpu.cs.lovexian.app.appadmin.utils;

import lombok.Data;

/**
 * Description:
 * User: 马鹏森 2020-09-25-8:47
 */

@Data
public class InfluxDbProperties {
    private String url;
    private String userName;
    private String password;
    private String database;
    private String retentionPolicy = "autogen";
    private String retentionPolicyTime = "30d";
    private int actions = 2000;
    private int flushDuration = 1000;
    private int jitterDuration = 0;
    private int bufferLimit = 10000;

    @Override
    public String toString() {
        return "InfluxDbProperties{" +
                "url='" + url + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", retentionPolicy='" + retentionPolicy + '\'' +
                ", retentionPolicyTime='" + retentionPolicyTime + '\'' +
                ", actions=" + actions +
                ", flushDuration=" + flushDuration +
                ", jitterDuration=" + jitterDuration +
                ", bufferLimit=" + bufferLimit +
                '}';
    }


}
