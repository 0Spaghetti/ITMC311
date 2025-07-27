package com.example.whichBiggerNumber;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import com.bumptech.glide.Glide;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    Button btnLeft, btnRight;
    TextView tvScore;
    int leftNum, rightNum, score = 0;
    Random random;

    MediaPlayer correctSound, wrongSound;
    Animation bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        tvScore = findViewById(R.id.tvScore);
        ImageView gifView = findViewById(R.id.gifView);
        Glide.with(this).asGif().load(R.drawable.sui).into(gifView);
        random = new Random();
        generateNewNumbers();

        // Load sounds and animation
        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
    }

    void checkAnswer(boolean leftPressed) {
        boolean correct = (leftPressed && leftNum > rightNum) || (!leftPressed && rightNum > leftNum);

        // Get clicked button
        Button clickedButton = leftPressed ? btnLeft : btnRight;

        // Always bounce on click
        clickedButton.startAnimation(bounce);

        if (correct) {
            score++;
            if (correctSound.isPlaying()) {
                correctSound.seekTo(0);  // Restart sound from beginning
            }
            correctSound.start();
            Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            score--;
            if (wrongSound.isPlaying()) {
                wrongSound.seekTo(0);    // Restart sound from beginning
            }
            wrongSound.start();
            Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
        }

        tvScore.setText(String.format("Points: %d", score));
        generateNewNumbers();
    }



    void generateNewNumbers() {
        leftNum = random.nextInt(9);
        do {
            rightNum = random.nextInt(9);
        } while (leftNum == rightNum);

        btnLeft.setText(String.valueOf(leftNum));
        btnRight.setText(String.valueOf(rightNum));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (correctSound != null) correctSound.release();
        if (wrongSound != null) wrongSound.release();
    }
}
