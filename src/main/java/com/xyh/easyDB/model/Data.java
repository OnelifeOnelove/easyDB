package com.xyh.easyDB.model;



import com.xyh.easyDB.annotation.Column;
import com.xyh.easyDB.annotation.Id;
import com.xyh.easyDB.annotation.Table;

import java.util.Date;

/**
 * Created by xiayuhui on 2016/8/24.
 */
@Table(value = "data")
public class Data {

    @Id
    private Integer id;
    private String name;
    private Integer type;
    private Integer version;
    private Integer tps;
    private Integer qps;
    @Column(value = "create_time")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getTps() {
        return tps;
    }

    public void setTps(Integer tps) {
        this.tps = tps;
    }

    public Integer getQps() {
        return qps;
    }

    public void setQps(Integer qps) {
        this.qps = qps;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
