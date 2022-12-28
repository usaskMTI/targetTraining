package com.example.cmpt381_a4;

import javafx.scene.input.*;

import java.util.ArrayList;

public class AppController {
    private Model model;
    private InteractionModel iModel;

    private double startX, startY;
    private double prevX, prevY;
    private double dX, dY;

    private double recX, recY;
    private double recWidth, recHeight;

    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.T)){
            System.out.println("Inside Control_T");
            iModel.setMode(InteractionModel.Mode.TEST);
        }
        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.E)){
            System.out.println("Inside Control_E");
            iModel.setMode(InteractionModel.Mode.EDIT);
        }
        else if (keyEvent.getCode().equals(KeyCode.DELETE)){
            System.out.println("DEL PRESSED");
            iModel.addUndoCommand(new DeleteCommand(model, iModel.getSelectedList()));
            model.deleteBlob(iModel.getSelectedList());
            iModel.clearSelections();
        }
        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.Z)){
            iModel.undo();
        }
        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.R)){
            iModel.redo();
        }

        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.X)){
            System.out.println("Cut");
            iModel.addUndoCommand(new DeleteCommand(model,iModel.getSelectedList())); // quick add
            // need to decide if I am doing reference style or clone style
            if (!iModel.getSelectedList().isEmpty()){
                model.cut(iModel.getSelectedList());
                iModel.setUpClipBoard("CUT");
            }
        }
        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.C)){
            System.out.println("Copy");
            if (!iModel.getSelectedList().isEmpty()){
                iModel.setUpClipBoard("COPY");
            }

        }
        else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.V)){
            iModel.getSelectedList().clear();
            iModel.getClipBoardContainer().forEach(clipBlob ->{
                model.pasteBlob(clipBlob.duplicate());
                iModel.addSelection(model.getLastBlob());
            });
            iModel.addUndoCommand(new CreateCommand(model,iModel.getSelectedList())); // quick add
            System.out.println("Paste");
        }
    }


    enum State {READY,PREPARE_CREATE, DRAGGING, RESIZING, RUBBER_BAND_LASSO}
    State currentState = State.READY;

    public AppController(){

    }

    public void setModel(Model newModel){
        this.model = newModel;
    }
    public void setIModel(InteractionModel newIModel){
        this.iModel = newIModel;
    }

    public void handlePressed(MouseEvent event) {

        switch (iModel.getCurrentMode()){

            case TEST -> {
                // do nothing as the trailController will handle the interaction
            }
            case EDIT -> {
                switch (currentState){
                    case READY -> {
                        // when mouse on blob
                        if (model.hitBlob(event.getX(),event.getY())){
                            Blob b = model.whichHit(event.getX(),event.getY());
                            if (event.isShiftDown()){

                                // for undo resize
                                startX = event.getX();
                                //
                                prevX = event.getX();
                                prevY = event.getY();
                                currentState = State.RESIZING;
                            }
                            else if (event.isControlDown()){
                                if (!iModel.getSelectedList().contains(b)){

                                    iModel.addSelection(b);
                                }
                                else {
                                    iModel.unselectBlob(b);
                                }
                                prevX = event.getX();
                                prevY = event.getY();
                                currentState = State.DRAGGING;
                            }
                            else { // just mouse click

                                // handling the case when you click on a unselected blob
                                if (iModel.getSelectedList().contains(b)){
                                    // do nothing
                                }
                                else if (!iModel.getSelectedList().contains(b)){
                                    iModel.clearSelections();
                                    iModel.addSelection(b);
                                }


                                // for undo move
                                startX = event.getX();
                                startY = event.getY();
                                //

                                prevX = event.getX();
                                prevY = event.getY();
                                currentState = State.DRAGGING;
                            }
                        }
                        else { // not on blob

                            // create blob part
                            if (event.isShiftDown()){
                                currentState = State.PREPARE_CREATE;
                            }
                            // when I thought rubber_band_lasso was by control clicking
//                            else if (event.isControlDown()){
//                                recX = event.getX();
//                                recY = event.getY();
//
//                                iModel.getPoints().clear();
//                                iModel.addPoints(event.getX(),event.getY());
//                                iModel.setPathComplete(false);
//
//                                currentState = State.RUBBER_BAND_LASSO;
//
//                            }
                            else { // handle rubber_band_lasso or clear selection
                                recX = event.getX();
                                recY = event.getY();

                                prevX = event.getX();
                                prevY = event.getY();

                                iModel.clearPoints();
                                iModel.addPoints(event.getX(),event.getY());
                                iModel.setPathComplete(false);
                                currentState = State.RUBBER_BAND_LASSO;
                            }
                        }
                    }
                }
            }
        }
    }

    public void handleDragged(MouseEvent event) {

        switch (iModel.getCurrentMode()){
            case EDIT ->{
                switch (currentState){
                    case PREPARE_CREATE -> {
                        currentState = State.READY;
                    }
                    case RESIZING-> {
                        if (event.isShiftDown()){
                            dX = event.getX() - prevX;
                            dY = event.getY() - prevY;
                            prevX = event.getX();
                            prevY = event.getY();

                            model.resizeBlob(iModel.getSelectedList(), dX);
                        }
                    }
                    case DRAGGING -> {
                        dX = event.getX() - prevX;
                        dY = event.getY() - prevY;
                        prevX = event.getX();
                        prevY = event.getY();
                        model.moveBlob(iModel.getSelectedList(), dX, dY);
                    }
                    case RUBBER_BAND_LASSO -> {
                        recWidth = event.getX() - recX;
                        recHeight = event.getY() - recY;
                        iModel.setRecComplete(false);
                        if (recWidth < 0 && recHeight < 0){
                            iModel.makeRectangleSelection(event.getX(), event.getY(),Math.abs(recWidth), Math.abs(recHeight));
                        }
                        else if (recWidth < 0){
                            iModel.makeRectangleSelection(event.getX(), event.getY()-Math.abs(recHeight),Math.abs(recWidth), Math.abs(recHeight));
                        }
                        else if (recHeight < 0){
                            iModel.makeRectangleSelection(event.getX()-Math.abs(recWidth), event.getY(),Math.abs(recWidth), Math.abs(recHeight));
                        }
                        else {
                            iModel.makeRectangleSelection(recX, recY,recWidth, recHeight);
                        }

                        // for the rubber band lasso
                        iModel.addPoints(event.getX(),event.getY());


                    }
                }
            }
        }
    }

    public void handleReleased(MouseEvent event) {
        switch (iModel.getCurrentMode()){
            case EDIT ->{
                switch (currentState){
                    case PREPARE_CREATE -> {
                        model.addBlob(event.getX(),event.getY());
                        Blob b = model.getBlobList().get(model.getBlobList().size()-1); // so that same blob is referred by the create command
                        iModel.addUndoCommand(new CreateCommand(model, b));
                        currentState = State.READY;
                    }
                    case RESIZING -> {
                        double resizeDX = event.getX() - startX;
                        iModel.addUndoCommand(new ResizeCommand(model, iModel.getSelectedList(), resizeDX));
                        currentState = State.READY;
                    }
                    case DRAGGING -> {
                        double undoDX = event.getX() - startX;
                        double undoDY = event.getY() - startY;

                        if (undoDX != 0 && undoDY != 0){ // to check if move actually happened if so then add the move command
                            System.out.println("Move command added");
                            iModel.addUndoCommand(new MoveCommand(model,iModel.getSelectedList(),undoDX, undoDY));
                        }
                        currentState = State.READY;
                    }

                    case RUBBER_BAND_LASSO -> {
                        System.out.println("End of lasso");
                        if (prevX == event.getX() && prevY == event.getY()) // to check if the cursor is in same position
                        {
                            System.out.println("same point: hence clear selection");
                            iModel.clearSelections();
                            iModel.clearPoints();
                            iModel.setRecComplete(true);
                            iModel.setPathComplete(true);
                            currentState = State.READY;
                        }
                        else { // handle rectangle_lasso selection
                            // rectangle band
                            if (recWidth < 0 && recHeight < 0){
                                recX = event.getX();
                                recY = event.getY();
                                recWidth = Math.abs(recWidth);
                                recHeight = Math.abs(recHeight);
                            }
                            else if (recWidth < 0){
                                recX = event.getX();
                                recY = event.getY()-Math.abs(recHeight);
                                recWidth = Math.abs(recWidth);
                                recHeight = Math.abs(recHeight);
                            }
                            else if (recHeight < 0){
                                recX = event.getX()-Math.abs(recWidth);
                                recY =  event.getY();
                                recWidth = Math.abs(recWidth);
                                recHeight = Math.abs(recHeight);
                            }
                            else {
                                recX = recX;
                                recY =  recY;
                                recWidth = recWidth;
                                recHeight = recHeight;
                            }
                            iModel.setRecComplete(true);
                            ArrayList<Blob> rectangleContainsBlob = model.rectangleContains(recX, recY, recWidth, recHeight);

                            // rubber band
                            iModel.setPathComplete(true);
                            iModel.updateReader();
                            ArrayList<Blob> rubberBandContainsBlob = model.rubberBandContains(iModel.getReader());

                            ArrayList<Blob> finalBlobs = null;
                            if (rectangleContainsBlob.size() >= rubberBandContainsBlob.size()){
                                finalBlobs = rectangleContainsBlob;
                            }
                            else {
                                finalBlobs = rubberBandContainsBlob;
                            }
                            finalBlobs.forEach(blob ->{
                                if (!iModel.getSelectedList().contains(blob)){
                                    iModel.addSelection(blob);
                                }
                                else {
                                    iModel.unselectBlob(blob);
                                }
                            });
                            currentState = State.READY;
                        }
                    }
                }
            }
        }

    }
}
