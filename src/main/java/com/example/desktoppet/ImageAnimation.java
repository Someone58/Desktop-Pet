package com.example.desktoppet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Arrays;

public class ImageAnimation {
    private PetController petController;
    private Timeline timeline;
    private final Image[] frames;
    private ImageView imageView;
    private int index = 0;
    private double fps;
    private String file;

    public ImageAnimation(PetController petController, String file, double fps) {
        this.petController = petController;
        imageView = petController.getPetImage();

        this.file = file;
        if (fps <= 0) throw new IllegalArgumentException("FPS must be > 0");
        this.fps = fps;

        frames = new Image[5];
        playAnimation();
        setupTimeline();
    }

    private void setupTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000.0 / fps), e -> {
            imageView.setImage(frames[index]);
            index = (index + 1) % frames.length;
        });

        timeline.getKeyFrames().add(keyFrame);
    }

    public void setFps(double newFps) {
        if (newFps <= 0) throw new IllegalArgumentException("FPS must be > 0");
        this.fps = newFps;
        setupTimeline();
        play();
    }

    public void play() { timeline.play(); }

    public void remove() {
        Arrays.fill(frames, null);
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    void playAnimation() {
        Arrays.fill(frames, null);
        for (int i = 0; i < 5; i++) {
            String path = "/Pets/" + file + "_Animation" + (i + 1) + ".png";
            var url = getClass().getResource(path);
            if (url == null) throw new IllegalArgumentException("Resource not found: " + path);
            frames[i] = new Image(url.toExternalForm());
        }
    }
    void playIdle(){
        Arrays.fill(frames, null);
        for (int i = 0; i < 5; i++) {
            String path = "/Pets/" + file + "_Idle" + (i + 1) + ".png";
            var url = getClass().getResource(path);
            if (url == null) throw new IllegalArgumentException("Resource not found: " + path);
            frames[i] = new Image(url.toExternalForm());
        }
    }
}
