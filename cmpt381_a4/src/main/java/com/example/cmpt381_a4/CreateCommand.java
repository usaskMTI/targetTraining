package com.example.cmpt381_a4;

import java.util.ArrayList;
import java.util.List;

public class CreateCommand implements TargetCommand{
    private Model model;
    private Blob b;
    private List<Blob> selectedList;

    public CreateCommand(Model m, Blob b){
        this.model = m;
        this.b = b;
        this.selectedList = new ArrayList<>();
        this.selectedList.add(b);
    }
    public CreateCommand(Model m, List<Blob> selectedList){
        this.model = m;
        this.selectedList = new ArrayList<>();
        selectedList.forEach(sBlob -> {
            this.selectedList.add(sBlob);
        });
    }

    @Override
    public void doIt() {
        this.model.addBlobs(selectedList);
//        this.model.getBlobList().add(b);
    }


    @Override
    public void undo() {
        this.model.deleteBlob(selectedList);
//        this.model.getBlobList().remove(b);
    }
}
