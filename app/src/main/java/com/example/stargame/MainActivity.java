package com.example.stargame;

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


public class MainActivity extends AppCompatActivity {

    private ImageView square_1_1;
    private ImageView square_1_2;
    private ImageView square_1_3;
    private ImageView square_2_1;
    private ImageView square_2_2;
    private ImageView square_2_3;
    private ImageView square_3_1;
    private ImageView square_3_2;
    private ImageView square_3_3;
    private ImageView currentSquare;
    private Button startButton;
    private TextView countView;

    private int count = 0;
    final private int minNumberOfRandom = 1;
    final private int maxNumberOfColor = 3;
    final private int maxNumberOfSquare = 9;
    boolean terminateThread = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startParams();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                unterminateTread();
                recfunc();
            }
        });

    }

    private void recfunc() {

        Thread run = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!terminateThread) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                defaultParamsForSquares();
                                int colorName = getStar();
                                currentSquare = getSquare();

                                final int resourseId = checkColor(colorName); // силка на coin

                                currentSquare.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Animation coinRiseAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.coin_clicked);
                                        currentSquare.startAnimation(coinRiseAnimation);
                                        if (resourseId == R.drawable.ic_silver_coin) {
                                            count = count + 1;
                                        }
                                        if (resourseId == R.drawable.ic_gold_coin) {
                                            count = count + 2;
                                        }
                                        if (resourseId == R.drawable.ic_red_coin) {
                                            count = count - 3;
                                        }
                                        checkPoints();
                                        countView.setText(Integer.toString(count));
                                        view.setOnClickListener(null);//Remove setOnClickListener
                                    }
                                });
                            }
                        });
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                defaultParamsForSquares();
                            }
                        });
                        Thread.sleep(1000); //1000 - 1 сек

                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        run.start(); // заводим
    }


    private void startParams() {

        currentSquare = findViewById(R.id.square_1_1);
        square_1_1 = findViewById(R.id.square_1_1);
        square_1_2 = findViewById(R.id.square_1_2);
        square_1_3 = findViewById(R.id.square_1_3);
        square_2_1 = findViewById(R.id.square_2_1);
        square_2_2 = findViewById(R.id.square_2_2);
        square_2_3 = findViewById(R.id.square_2_3);
        square_3_1 = findViewById(R.id.square_3_1);
        square_3_2 = findViewById(R.id.square_3_2);
        square_3_3 = findViewById(R.id.square_3_3);
        countView = findViewById(R.id.count);
        startButton = findViewById(R.id.start_button);

    }

    private ImageView getSquare() {

        int numberOfSquare = minNumberOfRandom + (int) (Math.random() * maxNumberOfSquare);//номер square
        ImageView iv = findViewById(R.id.square_1_1);
        if (numberOfSquare == 1) iv = square_1_1;
        if (numberOfSquare == 2) iv = square_1_2;
        if (numberOfSquare == 3) iv = square_1_3;
        if (numberOfSquare == 4) iv = square_2_1;
        if (numberOfSquare == 5) iv = square_2_2;
        if (numberOfSquare == 6) iv = square_2_3;
        if (numberOfSquare == 7) iv = square_3_1;
        if (numberOfSquare == 8) iv = square_3_2;
        if (numberOfSquare == 9) iv = square_3_3;
        return iv;

    }

    private void defaultParamsForSquares() {

        currentSquare.setClickable(false);
        square_1_1.setImageResource(R.drawable.square);
        square_1_2.setImageResource(R.drawable.square);
        square_1_3.setImageResource(R.drawable.square);
        square_2_1.setImageResource(R.drawable.square);
        square_2_2.setImageResource(R.drawable.square);
        square_2_3.setImageResource(R.drawable.square);
        square_3_1.setImageResource(R.drawable.square);
        square_3_2.setImageResource(R.drawable.square);
        square_3_3.setImageResource(R.drawable.square);

    }

    private int getStar() {

        int numberOfColor = minNumberOfRandom + (int) (Math.random() * maxNumberOfColor);
        return numberOfColor;

    }

    private int checkColor(int colorName) {

        int resourseId = 0;
        Animation shakeAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        currentSquare.startAnimation(shakeAnimation);
        if (colorName == 1) {
            currentSquare.setImageResource(R.drawable.ic_silver_coin);
            resourseId = R.drawable.ic_silver_coin;
        }
        if (colorName == 2) {
            resourseId = R.drawable.ic_gold_coin;
            currentSquare.setImageResource(R.drawable.ic_gold_coin);
        }
        if (colorName == 3) {
            resourseId = R.drawable.ic_red_coin;
            currentSquare.setImageResource(R.drawable.ic_red_coin);
        }
        return resourseId;

    }

    private void checkPoints() {

        if (count < 0) {
            terminateTread();
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
                countView.setText(Integer.toString(count));
                startButton.setVisibility(View.VISIBLE);
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private void terminateTread() {

        terminateThread = true;

    }

    private void unterminateTread() {

        terminateThread = false;

    }
}
