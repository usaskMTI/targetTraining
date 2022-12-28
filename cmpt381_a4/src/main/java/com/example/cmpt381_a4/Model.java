package com.example.cmpt381_a4;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<ModelListener> subscribers;
    private List<Blob> blobList;

    public Model(){
        subscribers = new ArrayList<>();
        blobList = new ArrayList<>();
    }

    public List<ModelListener> getSubscribers() {
        return subscribers;
    }

    public List<Blob> getBlobList() {
        return blobList;
    }


    public void addSubscriber(ModelListener sub) {
        subscribers.add(sub);
    }

    public void notifySubscribers(){
        subscribers.forEach(sub -> sub.modelChanged());
    }

    public boolean hitBlob(double x, double y) {
        for (Blob b : blobList) {
            if (b.contains(x,y)) return true;
        }
        return false;
    }
    public Blob whichHit(double x, double y) {
        for (Blob b : blobList) {
            if (b.contains(x,y)) return b;
        }
        return null;
    }

    public void moveBlob(List<Blob> selectedList, double dX, double dY) {
        selectedList.forEach(selectedBlob ->selectedBlob.move(dX,dY));
        notifySubscribers();
    }

    public void addBlob(double x, double y) {
        blobList.add(new Blob(x,y));
        notifySubscribers();
    }

    public void deleteBlob(List<Blob> selectedList) {
        selectedList.forEach(sBlob -> {
//            blobList.remove(sBlob);
            // re referencing the blob using the whichHit method to delete the blob as to delete method here
            // uses reference instead of using a equals method on blob class
            Blob b = whichHit(sBlob.getX(),sBlob.getY());
            if (b != null){
                blobList.remove(b);
            }
        });
        notifySubscribers();
    }

    public void resizeBlob(List<Blob> selectedList, double deltaX) {
        selectedList.forEach(selectedBlob ->selectedBlob.resize(deltaX));
        notifySubscribers();
    }

    public ArrayList<Blob> rectangleContains(double recX, double recY, double recWidth, double recHeight) {
        ArrayList<Blob> result = new ArrayList<>();
       blobList.forEach(blob -> {
           if (recX <= blob.getLeftPoint() && blob.getRightPoint() <= recX+recWidth){
               if (recY <= blob.getTopPoint() && blob.getBottomPoint() <= recY+recHeight){
                   result.add(blob);
               }
           }
       });
       return result;
    }

    public ArrayList<Blob> rubberBandContains(PixelReader areader) {
        ArrayList<Blob> result = new ArrayList<>();
        blobList.forEach(blob -> {
            if (areader.getColor((int) blob.getLeftPoint(), (int) blob.getY()).equals(Color.RED)){
                if (areader.getColor((int) blob.getRightPoint(), (int) blob.getY()).equals(Color.RED)){
                    if (areader.getColor((int) blob.getX(), (int) blob.getTopPoint()).equals(Color.RED)){
                        if (areader.getColor((int) blob.getX(), (int) blob.getBottomPoint()).equals(Color.RED)){
//                            System.out.println("Inside Blob");
                            result.add(blob);
                        }
                    }
                }
            }
        });
        return result;
    }

    public void cut(List<Blob> selectedList) {
        deleteBlob(selectedList);
    }

    public Blob getLastBlob(){
        return blobList.get(blobList.size()-1);
    }

    public void pasteBlob(Blob clipBlob) {
        blobList.add(clipBlob);
    }

    public void addBlobs(List<Blob> selectedList) {
        selectedList.forEach(sBlob -> {
            blobList.add(sBlob);
        });
    }
}
