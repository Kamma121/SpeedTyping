package Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AppSceneController {
    public static final String WORDS = "Words";
    public static final int COUNTDOWN_TIME_VALUE = 60;

    @FXML
    private Text seconds;
    private int countDownTime = COUNTDOWN_TIME_VALUE;
    private Timeline countdownTimeline;
    @FXML
    private Label modeLabel;

    @FXML
    private Label modePerMinuteLabel;

    @FXML
    public Text currentWord;
    @FXML
    private Text nextWord;
    @FXML
    private TextField input;
    private int allCounter = 0;
    private int correctCounter = 0;
    private int index = 2;
    @FXML
    private Text scorePerMinute;
    @FXML
    private Text accuracy;
    @FXML
    private ImageView image;
    @FXML
    private Text endOfTime;
    @FXML
    private Button playAgain;
    @FXML
    private Button backToMainMenu;

    List<String> words = new ArrayList<>();
    List<String> letters = new ArrayList<>();

    public void readWords() {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
            Collections.shuffle(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLetters() {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            letters.add(String.valueOf(letter));
        }
        Collections.shuffle(letters);
    }

    public void displayMode(String mode) {
        modeLabel.setText("Mode: " + mode);
        modePerMinuteLabel.setText(mode + " per minute");
    }

    public void countDown() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }
        countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    seconds.setText(Integer.toString(countDownTime));
                    countDownTime--;
                    if (countDownTime < 0) {
                        countdownTimeline.stop();
                        input.setVisible(false);
                        currentWord.setVisible(false);
                        nextWord.setVisible(false);
                        image.setVisible(false);
                        endOfTime.setVisible(true);
                        playAgain.setVisible(true);
                        backToMainMenu.setVisible(true);
                    }
                })
        );
        countdownTimeline.setCycleCount(countDownTime + 1);
        countdownTimeline.play();
    }

    public void initialize(String mode) {
        countDown();
        input.setStyle("-fx-text-inner-color: black;");
        playAgain.setFocusTraversable(false);
        backToMainMenu.setFocusTraversable(false);
        if (mode.equals(WORDS)) {
            readWords();
            currentWord.setText(words.get(0));
            nextWord.setText(words.get(1));
            startDetecting(words);
        } else {
            addLetters();
            currentWord.setText(letters.get(0));
            nextWord.setText(letters.get(1));
            startDetecting(letters);
        }
        displayMode(mode);
    }

    public void playAgainAction() {
        scorePerMinute.setText("");
        allCounter = 0;
        correctCounter = 0;
        accuracy.setText("");
        input.setVisible(true);
        input.clear();
        currentWord.setVisible(true);
        nextWord.setVisible(true);
        image.setVisible(true);
        image.setImage(null);
        endOfTime.setVisible(false);
        playAgain.setVisible(false);
        backToMainMenu.setVisible(false);
        countDownTime = COUNTDOWN_TIME_VALUE;
        countDown();
    }

    public void startDetecting(List<String> data) {
        input.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                String text = input.getText().trim();
                String current = currentWord.getText();
                if (text.equals(current)) {
                    correctCounter++;
                    Image correctAnswer = new Image(getClass().getResourceAsStream("/images/checked.png"));
                    image.setImage(correctAnswer);
                } else {
                    Image wrongAnswer = new Image(getClass().getResourceAsStream("/images/cross.png"));
                    image.setImage(wrongAnswer);
                }
                double ratio = (double) correctCounter / ++allCounter;
                DecimalFormat df = new DecimalFormat("#");
                accuracy.setText(df.format(ratio * 100));
                scorePerMinute.setText(Integer.toString(correctCounter));
                currentWord.setText(nextWord.getText());
                nextWord.setText(data.get(index++ % data.size()));
                input.clear();
                keyEvent.consume();
            }
        });
    }

    public void backToMainMenuAction(ActionEvent event) {
        correctCounter = 0;
        allCounter = 0;
        index = 2;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
