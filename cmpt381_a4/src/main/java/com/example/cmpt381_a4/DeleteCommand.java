package com.example.cmpt381_a4;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements TargetCommand{
    private Model model;
    private List<Blob> selectedList;

    public DeleteCommand(Model m, List<Blob> selectedList){
        this.model = m;
        this.selectedList = new ArrayList<>();
        selectedList.forEach(sBlob -> {
            this.selectedList.add(sBlob);
        });
    }
    @Override
    public void doIt() {
        model.deleteBlob(selectedList);
    }

    @Override
    public void undo() {
        selectedList.forEach(sBlob -> {
            // adding new blob using old data so reference style delete won't work
//            model.addBlob(sBlob.getX(), sBlob.getY());
            model.getBlobList().add(sBlob);
        });
    }
}
