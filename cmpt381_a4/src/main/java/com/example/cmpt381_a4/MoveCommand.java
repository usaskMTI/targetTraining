package com.example.cmpt381_a4;

import java.util.ArrayList;
import java.util.List;

public class MoveCommand implements TargetCommand{
    private double dX, dY;
    private Model model;
    private List<Blob> selectedList;

    public MoveCommand(Model m, List<Blob> selectedList, double dX, double dY){
        this.model = m;
//        this.selectedList = selectedList;
        this.selectedList = new ArrayList<>();
        selectedList.forEach(sBlob -> {
            this.selectedList.add(sBlob);
        });
        this.dX = dX;
        this.dY = dY;
    }
    @Override
    public void doIt() {
        model.moveBlob(selectedList, dX, dY);
    }

    @Override
    public void undo() {
        model.moveBlob(selectedList, -dX, -dY);
    }
}
