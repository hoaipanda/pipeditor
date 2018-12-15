package uk.co.senab.photoview.object;

import java.util.ArrayList;

public class BFrame {
    private String id;
    private String name;
    private int dotNumber;
    private int width;
    private int height;
    private ArrayList<BPoint> bPoints;
    private ArrayList<BLine> bLines;
    private String sequece;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDotNumber() {
        return dotNumber;
    }

    public void setDotNumber(int dotNumber) {
        this.dotNumber = dotNumber;
    }

    public ArrayList<BPoint> getbPoints() {
        return bPoints;
    }

    public void setbPoints(ArrayList<BPoint> bPoints) {
        this.bPoints = bPoints;
    }

    public String getSequece() {
        return sequece;
    }

    public void setSequece(String sequece) {
        this.sequece = sequece;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<BLine> getbLines() {
        return bLines;
    }

    public void setbLines(ArrayList<BLine> bLines) {
        this.bLines = bLines;
    }
}
