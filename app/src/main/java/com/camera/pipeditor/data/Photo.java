package com.camera.pipeditor.data;

import java.io.Serializable;

public class Photo implements Serializable {
    private String name;
    private boolean isPick;

    public Photo() {
    }

    public Photo(String name, boolean isPick) {
        this.name = name;
        this.isPick = isPick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }
}
