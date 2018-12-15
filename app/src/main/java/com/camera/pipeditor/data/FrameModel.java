package com.camera.pipeditor.data;

public class FrameModel {
    private float with;
    private float height;
    private float top;
    private float left;
    private String backGround;
    private String name;
    private String mask;
    private String title;

    public FrameModel(float with, float height, float top, float left, String backGround, String name, String mask, String title) {
        this.with = with;
        this.height = height;
        this.top = top;
        this.left = left;
        this.backGround = backGround;
        this.name = name;
        this.mask = mask;
        this.title = title;
    }

    public float getWith() {
        return with;
    }

    public float getHeight() {
        return height;
    }

    public float getTop() {
        return top;
    }

    public float getLeft() {
        return left;
    }

    public String getBackGround() {
        return backGround;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }
}
