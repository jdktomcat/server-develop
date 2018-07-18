package com.tmindtech.api.model;

import java.util.UUID;

/**
 * @author lwtang
 * @date 2018-04-02
 */
public class UUIDRes {

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private UUIDRes(String uuid) { this.uuid = uuid; }

    public static UUIDRes newInstance() {
        return new UUIDRes(UUID.randomUUID().toString());
    }

}
