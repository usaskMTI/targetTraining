package com.example.cmpt381_a4;

public class Blob {
    private double radius;
    private double x,y;
//    private  double orderID;

    public double getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    public Blob duplicate(){
        Blob duplicateBlob = new Blob(this.getX(),this.getY());
        duplicateBlob.setRadius(this.getRadius());
        return duplicateBlob;
    }
    public Blob(double nx, double ny){
        this.x = nx;
        this.y = ny;
        this.radius = 15;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void resize(double dX){
        radius += dX;
        if (radius < 5){
            radius = 5;
        }
    }
    public double getLeftPoint(){
        return this.x - this.radius;
    }
    public double getRightPoint(){
        return this.x + this.radius;
    }
    public double getTopPoint(){
        return this.y - this.radius;
    }
    public double getBottomPoint(){
        return this.y + this.radius;
    }
    public boolean contains(double cx, double cy) {
        return dist(cx,cy,x,y) <= radius;
    }
    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }


}
