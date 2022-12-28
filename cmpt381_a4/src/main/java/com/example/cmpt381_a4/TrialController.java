package com.example.cmpt381_a4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class TrialController {
    private Model model;
    private InteractionModel iModel;
    private double prevTime, currentTime;

    public TrialController(){

    }

    public void endOfTraining() {
        iModel.setMode(InteractionModel.Mode.REPORT);
//        System.out.println(iModel.getTrialRecordList());
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
                if (model.getBlobList().get(iModel.getCurrentIndex()).contains(event.getX(), event.getY())){
                    if (iModel.getCurrentIndex() == 0){
                        prevTime = System.currentTimeMillis();
//                        iModel.getTrialRecordList().clear();
                    }
                    else if (0 < iModel.getCurrentIndex() && iModel.getCurrentIndex() < model.getBlobList().size() ){
                        Blob prevBlob = model.getBlobList().get(iModel.getPrevIndex());
                        Blob currentBlob = model.getBlobList().get(iModel.getCurrentIndex());
                        currentTime = System.currentTimeMillis();
//                        System.out.println("Prev Time: " +prevTime);
//                        System.out.println("Current Time: "+currentTime);
//                        System.out.println("Elapsed Time: "+ (currentTime-prevTime));
                        iModel.addTrials(prevBlob,currentBlob, currentTime-prevTime);
                        prevTime = System.currentTimeMillis();
                    }
                    iModel.progressTrials();
//                    iModel.incrementIndex();
                }

            }
        }
    }
}
