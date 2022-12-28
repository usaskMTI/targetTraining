package com.example.cmpt381_a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TargetTrainerView extends StackPane implements IModelListener {
    private Canvas myCanvas;
    private GraphicsContext myGC;

    private Model model;
    private InteractionModel iModel;
    private TrialController tController;

    private int index;


    public TargetTrainerView(){
        myCanvas = new Canvas(800,800);
        myGC = myCanvas.getGraphicsContext2D();

        this.setStyle("-fx-background-color: #f5deb0");

        this.getChildren().add(myCanvas);
    }

    public void draw(){
        myGC.clearRect(0,0,800,800);
        myGC.save();
        index = iModel.getCurrentIndex();
        if (index < model.getBlobList().size()){
            Blob blob = model.getBlobList().get(index);
            myGC.setFill(Color.rgb(70, 129, 177));
            myGC.fillOval(blob.getX()-blob.getRadius(),blob.getY()-blob.getRadius(),blob.getRadius()*2,blob.getRadius()*2);
        }
        else if (!model.getBlobList().isEmpty() && index >= model.getBlobList().size() && iModel.getCurrentMode().equals(InteractionModel.Mode.TEST)) {
            tController.endOfTraining();
        }
        myGC.restore();
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


    public void setController(TrialController tController){
        this.tController = tController;
        myCanvas.setOnMousePressed(tController::handlePressed);
    }

    @Override
    public void iModelChanged() {
        draw();
    }

    public void startUp() {
        index = 0;
        iModel.setPrevIndex(0);
        iModel.setCurrentIndex(0);
        draw();
    }
}
