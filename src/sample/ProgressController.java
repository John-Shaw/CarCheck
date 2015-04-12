/**
 * Created by 建勇 on 2015/4/12.
 */
package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class ProgressController implements Initializable {
    public Button confirmBtn;
    public ProgressIndicator progressIc;
    private double progress;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progress=0.0;
        confirmBtn.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    try {
                        presentProgress();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }));
        timeline.setCycleCount(20);
        timeline.play();
    }

    private void presentProgress() throws InterruptedException {
        progress+=0.05;
        progressIc.setProgress(progress);
        if (progress>=1.0){
            confirmBtn.setVisible(true);
        }
    }

    public void confirm(ActionEvent actionEvent) {
        //todo 上传到数据库
        Platform.exit();
    }
}
