package com.example.cmpt381_a4;

import java.util.ArrayList;
import java.util.List;

public class TargetClipboard {
    private List<Blob> clipBoard;

    public TargetClipboard(){
        this.clipBoard = new ArrayList<>();
    }

    public void setCopyClipBoard(List<Blob> selectedList){
        clipBoard.clear();
        selectedList.forEach(sBlob -> {
            clipBoard.add(sBlob.duplicate());
        });
    }

    public void setCutClipBoard(List<Blob> selectedList) {
        clipBoard.clear();
        selectedList.forEach(sBlob -> {
            clipBoard.add(sBlob.duplicate());
//            clipBoard.add(sBlob);
        });
    }


    public List<Blob> getClipBoard() {
        return clipBoard;
    }
}
