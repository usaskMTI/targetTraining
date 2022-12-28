package com.example.cmpt381_a4;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane implements AppModeListener {
    private AppController controller;
    private TrialController tController;
    private Scene myScene;
    private Model model;
    private InteractionModel iModel;
    private DiagramView blobViews;
    private TargetTrainerView trainerView;
    private ReportView reportView;

    public MainUI(){
        blobViews = new DiagramView();
        trainerView = new TargetTrainerView();
        reportView = new ReportView();

        controller = new AppController();
        tController = new TrialController();

        model = new Model();
        iModel = new InteractionModel();


        blobViews.setModel(model);
        blobViews.setIModel(iModel);
        blobViews.setController(controller);

        trainerView.setModel(model);
        trainerView.setIModel(iModel);
        trainerView.setController(tController);

        reportView.setIModel(iModel);

        model.addSubscriber(blobViews);
        iModel.addSubscriber(blobViews);
        iModel.addSubscriber(trainerView);
        iModel.addModeSubscriber(this);

        controller.setModel(model);
        controller.setIModel(iModel);

        tController.setModel(model);
        tController.setIModel(iModel);

        this.getChildren().add(blobViews);

    }

    public void passScene(Scene scene) {
        this.myScene = scene;
        scene.setOnKeyPressed(controller::handleKeyPressed);
    }

    @Override
    public void appModeChanged() {
        switch (iModel.getCurrentMode()){
            case TEST -> {
                this.getChildren().clear();
                this.getChildren().add(trainerView);
                trainerView.startUp();
            }
            case EDIT -> {
                this.getChildren().clear();
                this.getChildren().add(blobViews);
            }
            case REPORT -> {
                this.getChildren().clear();
                this.getChildren().add(reportView);
                reportView.startUp();

            }
        }
    }
}
