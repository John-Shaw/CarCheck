package sample;

import com.google.gson.Gson;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public HBox titleView;
    public Label titleLabel;
    public Label option1Label;
    public ComboBox option1Combo;
    public Label option2Label;
    public ComboBox option2Combo;
    private int index;
    private int endIndex;
    private ArrayList<Node> nodes;
    private CarMes carMes;

    private Boolean isFillText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        index = 0;
        endIndex = 15;
        titleView.setStyle("-fx-background-color: #27478b;");
        titleLabel.setStyle("-fx-text-fill: white;");

        nodes = new ArrayList<Node>();
        nodes.add(titleLabel);
        nodes.add(option1Label);
        nodes.add(option2Label);
        nodes.add(option1Combo);
        nodes.add(option2Combo);

        readDataFromJson("samples.json");
        fillText(index);
        isFillText = false;

    }

    private void fillText(int index){
        titleLabel.setText(carMes.getType()[index].getName());
        option1Label.setText(carMes.getType()[index].getOption()[0].getLabel());
        option1Combo.setItems(FXCollections.observableArrayList(carMes.getType()[index].getOption()[0].getOption1(),
                carMes.getType()[index].getOption()[0].getOption2()));
        option1Combo.setPromptText(carMes.getType()[index].getOption()[0].getOption1());

        if (carMes.getType()[index].getOption()[1] != null) {
            option2Combo.setVisible(true);
            option2Label.setVisible(true);
            option2Label.setText(carMes.getType()[index].getOption()[1].getLabel());
            option2Combo.setItems(FXCollections.observableArrayList(carMes.getType()[index].getOption()[1].getOption1(),
                    carMes.getType()[index].getOption()[1].getOption2()));
            option2Combo.setPromptText(carMes.getType()[index].getOption()[1].getOption1());
        }
        else {
//            Alert mesBox = new Alert(Alert.AlertType.ERROR);
//            mesBox.setTitle("ERROR!");
//            mesBox.setContentText("please input ID number");
//            mesBox.showAndWait();
            option2Combo.setVisible(false);
            option2Label.setVisible(false);
            System.out.println("没有第二项");
        }
        isFillText = true;
    }

    private void animationWithNode(Node node){

        FadeTransition fadeTransition =
                new FadeTransition(Duration.millis(1000), node);
        fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.0f);

        TranslateTransition translateTransition =
                new TranslateTransition(Duration.millis(1000), node);
        translateTransition.setToX(600);
        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isFillText == true) {

                } else {
                    fillText(index);
                }
            }
        });


        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(1000), node);
        scaleTransition.setToX(2f);
        scaleTransition.setToY(2f);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                translateTransition,
                scaleTransition
        );



        FadeTransition fadeTransitionBack =
                new FadeTransition(Duration.millis(500), node);
        fadeTransitionBack.setFromValue(0.0f);
        fadeTransitionBack.setToValue(1.0f);
        TranslateTransition translateTransitionBack =
                new TranslateTransition(Duration.millis(500), node);
        translateTransitionBack.setToX(0);
        ScaleTransition scaleTransitionBack =
                new ScaleTransition(Duration.millis(500), node);
        scaleTransitionBack.setToX(1f);
        scaleTransitionBack.setToY(1f);
        ParallelTransition parallelTransitionBack = new ParallelTransition();
        parallelTransitionBack.getChildren().addAll(
                fadeTransitionBack,
                translateTransitionBack,
                scaleTransitionBack
        );


        SequentialTransition queue = new SequentialTransition();
        queue.getChildren().addAll(parallelTransition, parallelTransitionBack);
        queue.play();

    }

    private void animationWithNodes(ArrayList<Node> nodes){
        for(Node node:nodes){
            animationWithNode(node);
        }

    }

    public void nextOption(ActionEvent actionEvent) throws IOException {

        if(index>=endIndex){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Confirmation Dialog");
//            alert.setHeaderText("Look, a Confirmation Dialog");
            alert.setContentText("所有信息已录入。是否写入到数据库？");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Stage st= new Stage(StageStyle.DECORATED);
                Parent root = FXMLLoader.load(getClass().getResource("progress.fxml"));
                st.setTitle("Uploading");
                st.setScene(new Scene(root, 200, 200));
                st.show();
            } else {
                return;
            }
            return;
        }
        animationWithNodes(nodes);

        index++;
        isFillText = false;



    }


    private boolean postToMYSqlDB(){


        return true;
    }

    public void readDataFromJson(String name){
        String localPath = System.getProperty("user.dir").replaceAll("\\\\", "/");

        //读取json文件，保存到String json中
        String fileName=localPath+"/"+name;
        File file=new File(fileName);
        StringBuffer sb = new StringBuffer() ;
        String line;
        BufferedReader br=null;
        try {
            br=new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while((line=br.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json=sb.toString();

        Gson gson = new Gson();

        carMes=gson.fromJson(json, CarMes.class); //String转化成JavaBean



    }

    public void preOption(ActionEvent actionEvent) {
        index--;
        if(index < 0){
            index = 0;
            Alert mesBox = new Alert(Alert.AlertType.INFORMATION);
            mesBox.setTitle("ERROR!");
            mesBox.setContentText("所有选项已完成");
            mesBox.showAndWait();
            return;
        }
        animationWithNodes(nodes);
        isFillText = false;
    }
}
