package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  Entity
 *
 * @author xpu
 * @date 2020-09-01 21:51:18
 */
@Data
@TableName("gateway_dtu")
public class AdminGatewayDtu {
    /**
     * 每条数据的id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 网关标识
     */
    @TableId(value = "gateway_id", type = IdType.ID_WORKER_STR)
    private String gatewayId;

    /**
     * dtu标识
     */
    @TableId(value = "dtu_id", type = IdType.ID_WORKER_STR)
    private String dtuId;
}
