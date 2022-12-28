package com.example.cmpt381_a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class ReportView extends StackPane {

    private ScatterChart<Number,Number> sc;
    private XYChart.Series series;
    private InteractionModel iModel;

    public ReportView(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        yAxis.setLabel("MT (ms)");
        xAxis.setLabel("ID (bits)");

        sc = new ScatterChart<>(xAxis,yAxis);
        sc.setTitle("Targeting Performance");
        series = new XYChart.Series();
        series.setName("Trial Performance");
        sc.getData().add(series);
        this.getChildren().add(sc);
    }

    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void startUp() {
        series.getData().clear();
        iModel.getTrialRecordList().forEach(trial -> {
            series.getData().add(new XYChart.Data(trial.getID(),trial.getElapsedTime()));
        });

    }
}
