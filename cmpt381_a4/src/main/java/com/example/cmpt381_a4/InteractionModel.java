package com.example.cmpt381_a4;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InteractionModel {
    private List<AppModeListener> modeSubscribers;
    private List<Blob> selectedList;
    private List<TrialRecord> trialRecordList;
    private double recX, recY, recWidth, recHeight;
    private boolean recComplete;
    private int prevIndex, currentIndex;

    private PixelReader reader;
    private List<Point2D> points;
    private boolean pathComplete;

    private List<IModelListener> subscribers;
    private Stack<TargetCommand> undoStack;
    private Stack<TargetCommand> redoStack;
    private TargetClipboard clipBoard;


    public InteractionModel(){
        subscribers = new ArrayList<>();
        selectedList = new ArrayList<>();
        points =  new ArrayList<>();
        modeSubscribers = new ArrayList<>();
        trialRecordList = new ArrayList<>();

        undoStack = new Stack<>();
        redoStack = new Stack<>();

        clipBoard = new TargetClipboard();

        pathComplete = true;
    }


    public Stack<TargetCommand> getUndoStack() {
        return undoStack;
    }

    public Stack<TargetCommand> getRedoStack() {
        return redoStack;
    }

    public void setPrevIndex(int prevIndex) {
        this.prevIndex = prevIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public PixelReader getReader() {
        return reader;
    }

    public void setReader(PixelReader reader) {
        this.reader = reader;
    }





    public int getPrevIndex() {
        return prevIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }


    public boolean isRecComplete() {
        return recComplete;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public boolean isPathComplete() {
        return pathComplete;
    }

    public void setPathComplete(boolean pathComplete) {
        this.pathComplete = pathComplete;
    }

    public double getRecX() {
        return recX;
    }

    public double getRecY() {
        return recY;
    }

    public double getRecWidth() {
        return recWidth;
    }

    public double getRecHeight() {
        return recHeight;
    }

    public List<TrialRecord> getTrialRecordList() {
        return trialRecordList;
    }

    public void setRecComplete(boolean recComplete) {
        this.recComplete = recComplete;
        notifySubscribers();
    }



    public List<IModelListener> getSubscribers() {
        return subscribers;
    }

    public List<Blob> getSelectedList() {
        return selectedList;
    }

    public void addSubscriber(IModelListener sub){
        subscribers.add(sub);
    }

    public void notifySubscribers(){
        subscribers.forEach(sub -> sub.iModelChanged());
    }

    public void addSelection(Blob selectedBlob) {
        selectedList.add(selectedBlob);
        notifySubscribers();
    }

    public void clearSelections() {
        selectedList.clear();
        notifySubscribers();
    }

    public void unselectBlob(Blob b) {
        if (selectedList.contains(b)){
            selectedList.remove(b);
        }
        notifySubscribers();
    }


    public void makeRectangleSelection(double recX, double recY, double recWidth, double recHeight) {
        this.recX = recX;
        this.recY = recY;
        this.recWidth = recWidth;
        this.recHeight = recHeight;
        notifySubscribers();

    }


    public void updateReader() {
//        System.out.println("Old: "+reader);
        notifySubscribers();
//        System.out.println("New: "+reader);
    }

    public void addPoints(double x, double y) {
        Point2D newPoints = new Point2D(x,y);
        this.points.add(newPoints);
        notifySubscribers();
    }

    public void addModeSubscriber(AppModeListener modeSub) {
        modeSubscribers.add(modeSub);
    }

    public void setMode(Mode test) {
        currentMode = test;
        notifyModeSubscribers();
    }

    private void notifyModeSubscribers() {
        modeSubscribers.forEach(mSub ->{
            mSub.appModeChanged();
        });
    }

    public void progressTrials() {
        incrementIndex();
        notifySubscribers();

    }
    public void incrementIndex(){
        if (currentIndex > 0){
            prevIndex += 1;
        }
        this.currentIndex += 1;
    }

    public void addTrials(Blob prevBlob, Blob currentBlob, double elapsedTime) {
        double ID = (2*dist(prevBlob.getX(),prevBlob.getY(),currentBlob.getX(),currentBlob.getY()))/currentBlob.getRadius()*2;
        trialRecordList.add(new TrialRecord(elapsedTime,ID));
    }
    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    public void addUndoCommand(TargetCommand command) {
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()){
            TargetCommand command = undoStack.pop();
            System.out.println(command.getClass());
            redoStack.push(command);
            command.undo();
            notifySubscribers();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()){
            TargetCommand command = redoStack.pop();
            System.out.println(command.getClass());
            undoStack.push(command);
            command.doIt();
            notifySubscribers();
        }
        else {
            System.out.println("REDO is empty");
        }
    }



    public void setUpClipBoard(String cutCopy) {
        if (cutCopy.equals("CUT"))
        {
            this.clipBoard.setCutClipBoard(selectedList);
            selectedList.clear();
        }
        else if (cutCopy.equals("COPY")){
            this.clipBoard.setCopyClipBoard(selectedList);
        }



    }
    public List<Blob> getClipBoardContainer(){
        return this.clipBoard.getClipBoard();
    }

    public void clearPoints() {
        points.clear();
    }

    enum Mode {EDIT,TEST,REPORT}
    Mode currentMode = Mode.EDIT;

    public Mode getCurrentMode() {
        return currentMode;
    }
}
