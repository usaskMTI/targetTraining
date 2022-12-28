package com.example.cmpt381_a4;

import java.util.ArrayList;
import java.util.List;

public class ResizeCommand implements TargetCommand{
    private Model model;
    private double dX;
    private List<Blob> selectedList;

    public ResizeCommand(Model m,List<Blob> selectedList, double resize){
        this.dX = resize;
        this.model = m;
        this.selectedList = new ArrayList<>();
        selectedList.forEach(sBlob -> {
            this.selectedList.add(sBlob);
        });
    }


    @Override
    public void doIt() {
        model.resizeBlob(selectedList, dX);
    }

    @Override
    public void undo() {
        model.resizeBlob(selectedList, -dX);
    }
}
