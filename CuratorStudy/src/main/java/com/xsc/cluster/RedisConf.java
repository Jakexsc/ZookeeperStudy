package com.xsc.cluster;

/**
 * @author JakeXsc
 * @version 1.0
 * @date 2020/8/13 23:06
 */
public class RedisConf {
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 路径
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
