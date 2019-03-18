package com.example.stargame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_NUMBER_OF_COLOR = 3;

    private ImageView currentSquare;
    private Button    startButton;
    private TextView  countView;

    private int count;

    private volatile boolean isGameTerminated;

    private final List<ImageView> squares = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    private void startGame() {
        startButton.setVisibility(View.GONE);
        isGameTerminated = false;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isGameTerminated) {
                    try {
                        processThread();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        thread.start(); // заводим
    }

    private void processThread() throws InterruptedException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetViewsToDefault();
                currentSquare = getSquare();

                final SquareProperty property = getSquareProperty(); // силка на coin

                currentSquare.setImageResource(property.getImageRes());
                Animation shakeAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                currentSquare.startAnimation(shakeAnimation);

                currentSquare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation riseAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.coin_clicked);
                        currentSquare.startAnimation(riseAnimation);
                        addPoints(property.getPoints());
                        countView.setText(String.valueOf(count));
                        view.setOnClickListener(null); // remove click listener
                    }
                });
            }
        });
        Thread.sleep(1000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetViewsToDefault();
            }
        });
        Thread.sleep(1000); //1000 - 1 сек
    }

    @SuppressLint("CutPasteId")
    private void initViews() {
        ImageView square_1_1 = findViewById(R.id.square_1_1);
        ImageView square_1_2 = findViewById(R.id.square_1_2);
        ImageView square_1_3 = findViewById(R.id.square_1_3);
        ImageView square_2_1 = findViewById(R.id.square_2_1);
        ImageView square_2_2 = findViewById(R.id.square_2_2);
        ImageView square_2_3 = findViewById(R.id.square_2_3);
        ImageView square_3_1 = findViewById(R.id.square_3_1);
        ImageView square_3_2 = findViewById(R.id.square_3_2);
        ImageView square_3_3 = findViewById(R.id.square_3_3);

        squares.add(square_1_1);
        squares.add(square_1_2);
        squares.add(square_1_3);
        squares.add(square_2_1);
        squares.add(square_2_2);
        squares.add(square_2_3);
        squares.add(square_3_1);
        squares.add(square_3_2);
        squares.add(square_3_3);

        currentSquare = findViewById(R.id.square_1_1);
        countView = findViewById(R.id.count);
        startButton = findViewById(R.id.start_button);
    }

    private ImageView getSquare() {
        int numberOfSquare = (int) (Math.random() * squares.size()) + 1; // square index [1,9]
        return squares.get(numberOfSquare - 1);
    }

    private void resetViewsToDefault() {
        for (ImageView iv : squares) {
            iv.setOnClickListener(null);
            iv.setImageResource(R.drawable.square);
        }
    }

    private SquareProperty getSquareProperty() {
        int index = (int) (Math.random() * MAX_NUMBER_OF_COLOR) + 1; // coin index [1,3]
        if (index == 1) {
            return new SquareProperty(R.drawable.ic_silver_coin, 1);
        } else if (index == 2) {
            return new SquareProperty(R.drawable.ic_gold_coin, 2);
        } else if (index == 3) {
            return new SquareProperty(R.drawable.ic_red_coin, -3);
        } else {
            throw new IllegalStateException("Invalid index");
        }
    }

    private void addPoints(int toAdd) {
        count += toAdd;
        if (count < 0) {
            terminateThread();
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.game_over);
        alert.setCancelable(false);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                count = 0;
                countView.setText(String.valueOf(count));
                startButton.setVisibility(View.VISIBLE);
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void terminateThread() {
        isGameTerminated = true;
    }
}
