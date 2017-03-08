package com.memo.dayandnight.mode;

/**
 * Created by lbl on 2017/1/13.
 */

public enum DayNight {
    DAY("DAY", 0), NIGHT("NIGHT", 1);

    private String name;
    private int code;

    DayNight(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
