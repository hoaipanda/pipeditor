package com.camera.pipeditor.data;

public class Menu {
    private int icon;
    private int icon_sl;

    public Menu() {
    }

    public Menu(int icon, int icon_sl) {
        this.icon = icon;
        this.icon_sl = icon_sl;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon_sl() {
        return icon_sl;
    }

    public void setIcon_sl(int icon_sl) {
        this.icon_sl = icon_sl;
    }
}
