package com.example.cmpt381_a4;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DiagramView extends StackPane implements ModelListener, IModelListener{
    private Canvas myCanvas;
    private GraphicsContext myGC;

    private Model model;
    private InteractionModel iModel;
    private AppController controller;

    private int orderNumber;

    public DiagramView(){
        myCanvas = new Canvas(800,800);
        myGC = myCanvas.getGraphicsContext2D();
        this.setStyle("-fx-background-color: skyblue");
        this.getChildren().addAll(myCanvas);
    }


    public void draw(){
        myGC.clearRect(0,0,800,800);
        myGC.setFill(Color.SKYBLUE);
        myGC.fillRect(0,0,800,800);
        orderNumber = 1;
        model.getBlobList().forEach(blob -> {
            if (iModel.getSelectedList().contains(blob)){
                myGC.setFill(Color.PINK);
            }
            else {
                myGC.setFill(Color.SKYBLUE.darker());
            }
            myGC.fillOval(blob.getX()-blob.getRadius(),blob.getY()-blob.getRadius(),blob.getRadius()*2,blob.getRadius()*2);
            myGC.strokeText(String.valueOf(orderNumber), blob.getX()-3, blob.getY()+5);
            orderNumber += 1;
        });

        if (!iModel.isRecComplete()){
            myGC.save();
            myGC.setStroke(Color.GREEN);
            myGC.strokeRect(iModel.getRecX(),iModel.getRecY(),iModel.getRecWidth(), iModel.getRecHeight());
            myGC.restore();
        }
        if (!iModel.isPathComplete()){
            if (!iModel.getPoints().isEmpty()){ // exception handle for no points
                myGC.save();
                myGC.setStroke(Color.RED);
                myGC.beginPath();
                myGC.moveTo(iModel.getPoints().get(0).getX(),iModel.getPoints().get(0).getY());
                iModel.getPoints().forEach(p -> myGC.lineTo(p.getX(),p.getY()));
                myGC.stroke();
                WritableImage buffer = myCanvas.snapshot(null,null);
                iModel.setReader(buffer.getPixelReader());
                myGC.restore();
            }

        }
        else if (iModel.isPathComplete()) {

            if (!iModel.getPoints().isEmpty()){  // exception handle for no points
                Stage stage = new Stage();
                StackPane pane = new StackPane();
                Scene scene = new Scene(pane,800,800);

//                System.out.println("Inside the bitmap");
                Canvas checkCanvas = new Canvas(800,800);
                GraphicsContext checkGC = checkCanvas.getGraphicsContext2D();

                checkGC.save();
                checkGC.setFill(Color.RED);
                checkGC.beginPath();
                checkGC.moveTo(iModel.getPoints().get(0).getX(),iModel.getPoints().get(0).getY());
                iModel.getPoints().forEach(p -> checkGC.lineTo(p.getX(),p.getY()));
                checkGC.closePath();
                checkGC.fill();
                WritableImage buffer = checkCanvas.snapshot(null, null);
//                iModel.reader = buffer.getPixelReader();
                iModel.setReader(buffer.getPixelReader());
//                myGC.drawImage(buffer,0,0);

                pane.getChildren().add(checkCanvas);
                stage.setScene(scene);
//                stage.show();
                checkGC.restore();
            }
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


    public void setController(AppController controller){
        this.controller = controller;
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }

    @Override
    public void modelChanged() {
        draw();
    }

    @Override
    public void iModelChanged() {
        draw();
    }
}
