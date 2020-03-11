package com.example.quizzes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RelativeLayout myLayout;
    AnimationDrawable mAnimationDrawable;
    CardView mCardView1;
    CardView mCardView5;
    CardView mCardView2;
    CardView mCardView3;
    CardView mCardView4;
    CardView mCardView6;
    CardView mCardView7;
    CardView mCardView8;
    int flag=1;
    ProgressBar progressBar;
    ImageView imageView;
    TextView question;
    TextView option1,option2,option3,option4;
    int correctPostion;
    String correctAnswer;
    TextView correct;
    TextView timer;
    TextView highScore;
    int totalQuestionAsked,noOfCorrectAnswer;
    int highScore1;
    CountDownTimer countDownTimer;
    int flag1=0;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    public static final String pref="My Pref File";
    String imageUrl;
    //int y=-1;
    DatabaseReference reference;
    //String questions1,options1,options2,options3,options4;
    //String key;
    List<Questions> questionsList;
    Questions questions;
    CardView finalScoreCardView;
    CardView finalScore;
    CardView playAgainCardView;
    CardView exitCardView;
    TextView finalScoreTextView;
    TextView playAgainTextView;
    TextView exitTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        correct = findViewById(R.id.correct);
        timer = findViewById(R.id.timer);
        highScore = findViewById(R.id.highScore);
        myLayout =  findViewById(R.id.myLayout);
       // myLayout.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        questionsList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        mCardView1 =  findViewById(R.id.cardView1);
        mCardView2 = findViewById(R.id.cardView2);
        mCardView3 = findViewById(R.id.cardView3);
        mCardView4 = findViewById(R.id.cardView4);
        mCardView5 = findViewById(R.id.cardView5);
        mCardView6 = findViewById(R.id.cardView6);
        mCardView7 = findViewById(R.id.cardView7);
        mCardView8 = findViewById(R.id.cardView8);
        mCardView1.setVisibility(View.INVISIBLE);
        mCardView2.setVisibility(View.INVISIBLE);
        mCardView3.setVisibility(View.INVISIBLE);
        mCardView4.setVisibility(View.INVISIBLE);
        mCardView5.setVisibility(View.INVISIBLE);
        mCardView6.setVisibility(View.INVISIBLE);
        mCardView7.setVisibility(View.INVISIBLE);
        mCardView8.setVisibility(View.INVISIBLE);
        finalScoreCardView=findViewById(R.id.finalScoreCard);
        finalScore = findViewById(R.id.finalScore);
        finalScoreTextView = findViewById(R.id.finalScoreTextView);
        playAgainCardView = findViewById(R.id.playAgainCardView);
        playAgainTextView = findViewById(R.id.playAgainText);
        exitCardView = findViewById(R.id.exitCardView);
        exitTextView = findViewById(R.id.exitTextView);
        totalQuestionAsked=0;
        noOfCorrectAnswer=0;
        //playAgain = findViewById(R.id.playAgain);
        preferences = getSharedPreferences(pref,MODE_PRIVATE);
        String x = preferences.getString("highScore","0");;
        highScore1 = Integer.parseInt(x);
        highScore.setText(x);
        reference = FirebaseDatabase.getInstance().getReference().child("questions");
        feedArrayList();
        //myLayout.setVisibility(View.VISIBLE);
//        mAnimationDrawable = (AnimationDrawable) myLayout.getBackground();
//        mAnimationDrawable.setEnterFadeDuration(1000);
//        mAnimationDrawable.setExitFadeDuration(2000);
//        mAnimationDrawable.start();
//        mCardView1 =  findViewById(R.id.cardView1);
//        mCardView1.setTranslationX(-1000f);
//        ObjectAnimator animation = ObjectAnimator.ofFloat(mCardView1, "translationX", 0f);
//        animation.setDuration(500);
//        animation.start();
//        mCardView2 =  findViewById(R.id.cardView2);
//        mCardView2.setTranslationX(-1000f);
//        ObjectAnimator animation1 = ObjectAnimator.ofFloat(mCardView2, "translationX", 0f);
//        animation1.setDuration(1000);
//        animation1.start();
//        mCardView3 =  findViewById(R.id.cardView3);
//        mCardView3.setTranslationX(-1000f);
//        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mCardView3, "translationX", 0f);
//        animation2.setDuration(1500);
//        animation2.start();
//        mCardView4 =  findViewById(R.id.cardView4);
//        mCardView4.setTranslationX(-1000f);
//        ObjectAnimator animation3 = ObjectAnimator.ofFloat(mCardView4, "translationX", 0f);
//        animation3.setDuration(2000);
//        animation3.start();
//        mCardView5 = findViewById(R.id.cardView5);
//        mCardView5.setTranslationX(-1000f);
//        ObjectAnimator animation4 = ObjectAnimator.ofFloat(mCardView5, "translationX", 0f);
//        animation4.setDuration(2500);
//        animation4.start();




        Log.i("TAG",Integer.toString(questionsList.size()));

        flag=1;

        playAgainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                playAgainCardView.setCardBackgroundColor(Color.rgb(85,139,47));
                new CountDownTimer(10,10) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        finalScoreCardView.setVisibility(View.GONE);
                        playAgainCardView.setCardBackgroundColor(Color.rgb(44, 62, 80));
                        noOfCorrectAnswer = 0;
                        totalQuestionAsked = 0;
                        mCardView2.setEnabled(true);
                        mCardView3.setEnabled(true);
                        mCardView4.setEnabled(true);
                        mCardView5.setEnabled(true);
                        correct.setText("Your Score: " + Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
                        mCardView2.setCardBackgroundColor(Color.rgb(44, 62, 80));
                        mCardView3.setCardBackgroundColor(Color.rgb(44, 62, 80));
                        mCardView4.setCardBackgroundColor(Color.rgb(44, 62, 80));
                        mCardView5.setCardBackgroundColor(Color.rgb(44, 62, 80));
                        generateQuestion();
                    }
                }.start();
            }
        });

        exitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to Exit?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                finish();
                                System.exit(0);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag,v);
            }
        });

//        option1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String tag = v.getTag().toString();
//                checkAnswer(tag);
//            }
//        });

        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag,v);
            }
        });

        mCardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag,v);
            }
        });

        mCardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag,v);
            }
        });
    }
    public void feedArrayList(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    String q1 = snapshot.child("questions").getValue().toString();
                    String o1 = snapshot.child("option1").getValue().toString();
                    String o2 = snapshot.child("option2").getValue().toString();
                    String o3 = snapshot.child("option3").getValue().toString();
                    String o4 = snapshot.child("option4").getValue().toString();
                    String a = snapshot.child("answer").getValue().toString();
                    String im = snapshot.child("imageUrl").getValue().toString();
                    questions = new Questions(q1,o1,o2,o3,o4,a,im);
                    questionsList.add(questions);
                }
                mCardView1.setVisibility(View.VISIBLE);
                mCardView2.setVisibility(View.VISIBLE);
                mCardView3.setVisibility(View.VISIBLE);
                mCardView4.setVisibility(View.VISIBLE);
                mCardView5.setVisibility(View.VISIBLE);
                mCardView6.setVisibility(View.VISIBLE);
                mCardView7.setVisibility(View.VISIBLE);
                mCardView8.setVisibility(View.VISIBLE);
                Log.i("TAG",Integer.toString(questionsList.size()));
                generateQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveHighScore(){
        editor = getSharedPreferences(pref,MODE_PRIVATE).edit();
        editor.putString("highScore",Integer.toString(highScore1));
        editor.apply();
    }

    public void showToast(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void generateQuestion(){
        Random random = new Random();
        int x = random.nextInt(questionsList.size());
        questions = questionsList.get(x);
        question.setText(questions.getQuestions());
        option1.setText(questions.getOption1());
        option2.setText(questions.getOption2());
        option3.setText(questions.getOption3());
        option4.setText(questions.getOption4());
        correctAnswer = questions.getAnswer();

        if(questions.getOption1().equals(correctAnswer)){
            correctPostion = 1;
        }
        else if(questions.getOption2().equals(correctAnswer)){
            correctPostion = 2;
        }
        else if(questions.getOption3().equals(correctAnswer)){
            correctPostion = 3;
        }
        else if(questions.getOption4().equals(correctAnswer)){
            correctPostion = 4;
        }
        imageUrl = questions.getImageUrl();
        try {
            DownloadImage image = new DownloadImage();
            Bitmap myBitmap = image.execute(imageUrl).get();
            imageView.setImageBitmap(myBitmap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        totalQuestionAsked++;
        progressBar.setVisibility(View.INVISIBLE);
        showAnimation();

        startTimer();

    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timer.setText("0s");
                mCardView2.setEnabled(false);
                mCardView3.setEnabled(false);
                mCardView4.setEnabled(false);
                mCardView5.setEnabled(false);
                mCardView2.setCardBackgroundColor(Color.rgb(179,158,158));
                mCardView3.setCardBackgroundColor(Color.rgb(179,158,158));
                mCardView4.setCardBackgroundColor(Color.rgb(179,158,158));
                mCardView5.setCardBackgroundColor(Color.rgb(179,158,158));
                correct.setText("Your Score: " + Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
                showFinalScore();
            }
        }.start();
    }

    public class DownloadImage extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void checkAnswer(String tag,View v){
        mCardView2.setEnabled(false);
        mCardView3.setEnabled(false);
        mCardView4.setEnabled(false);
        mCardView5.setEnabled(false);
        mCardView2.setCardBackgroundColor(Color.rgb(179,158,158));
        mCardView3.setCardBackgroundColor(Color.rgb(179,158,158));
        mCardView4.setCardBackgroundColor(Color.rgb(179,158,158));
        mCardView5.setCardBackgroundColor(Color.rgb(179,158,158));
        countDownTimer.cancel();
        if(tag.equals("1")){
            mCardView2.setCardBackgroundColor(Color.rgb(98,0,238));
        }
        else if(tag.equals("2")){
            mCardView3.setCardBackgroundColor(Color.rgb(98,0,238));
        }
        else if(tag.equals("3")){
            mCardView4.setCardBackgroundColor(Color.rgb(98,0,238));
        }
        else{
            mCardView5.setCardBackgroundColor(Color.rgb(98,0,238));
        }
        startAnotherTimer(tag,v);

    }

    private void startAnotherTimer(final String tag, final View v) {
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
               new CountDownTimer(1000,200){

                   @Override
                    public void onTick(long millisUntilFinished1){
                        if(flag1==0){
                            if(tag.equals("1")){
                                mCardView2.setCardBackgroundColor(Color.rgb(98,0,238));
                            }
                            else if(tag.equals("2")){
                                mCardView3.setCardBackgroundColor(Color.rgb(98,0,238));
                            }
                            else if(tag.equals("3")){
                                mCardView4.setCardBackgroundColor(Color.rgb(98,0,238));
                            }
                            else{
                                mCardView5.setCardBackgroundColor(Color.rgb(98,0,238));
                            }
                            flag1=1;
                        }
                        else{
                            if(tag.equals("1")){
                                mCardView2.setCardBackgroundColor(Color.rgb(44,62,80));
                            }
                            else if(tag.equals("2")){
                                mCardView3.setCardBackgroundColor(Color.rgb(44,62,80));
                            }
                            else if(tag.equals("3")){
                                mCardView4.setCardBackgroundColor(Color.rgb(44,62,80));
                            }
                            else{
                                mCardView5.setCardBackgroundColor(Color.rgb(44,62,80));
                            }
                            flag1=0;
                        }
                    }

                   @Override
                   public void onFinish() {

                   }
               }.start();

            }

            @Override
            public void onFinish() {
                if(Integer.toString(correctPostion).equals("1")){
                    mCardView2.setCardBackgroundColor(Color.rgb(85,139,47));
                }
                else if(Integer.toString(correctPostion).equals("2")){
                    mCardView3.setCardBackgroundColor(Color.rgb(85,139,47));
                }
                else if(Integer.toString(correctPostion).equals("3")){
                    mCardView4.setCardBackgroundColor(Color.rgb(85,139,47));
                }
                else{
                    mCardView5.setCardBackgroundColor(Color.rgb(85,139,47));
                }
                if(tag.equals(Integer.toString(correctPostion))){
                    // progressBar.setVisibility(View.VISIBLE);
                    //showToast("Correct");
                    noOfCorrectAnswer++;
                    if(highScore1<noOfCorrectAnswer){
                        highScore1=noOfCorrectAnswer;
                    }
                    saveHighScore();
                    correct.setText("Your Score: " +Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
                    highScore.setText(Integer.toString(highScore1));
                    startThirdTimer(v);
                }
                else{
                    //showToast("Incorrect! correct answer is " + correctAnswer);
                    //countDownTimer.cancel();
                    if(tag.equals("1")){
                        mCardView2.setCardBackgroundColor(Color.rgb(216,67,21));
                    }
                    else if(tag.equals("2")){
                        mCardView3.setCardBackgroundColor(Color.rgb(216,67,21));
                    }
                    else if(tag.equals("3")){
                        mCardView4.setCardBackgroundColor(Color.rgb(216,67,21));
                    }
                    else{
                        mCardView5.setCardBackgroundColor(Color.rgb(216,67,21));
                    }
                    //startThirdTimer(false);
                    //countDownTimer.cancel();
                    correct.setText("Your Score: " + Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
                    highScore.setText(Integer.toString(highScore1));
                    showFinalScore();
                }

            }
        }.start();
    }

    private void startThirdTimer(final View v) {
        new CountDownTimer(200,100){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mCardView2.setEnabled(true);
                mCardView3.setEnabled(true);
                mCardView4.setEnabled(true);
                mCardView5.setEnabled(true);
                mCardView2.setCardBackgroundColor(Color.rgb(44,62,80));
                mCardView3.setCardBackgroundColor(Color.rgb(44,62,80));
                mCardView4.setCardBackgroundColor(Color.rgb(44,62,80));
                mCardView5.setCardBackgroundColor(Color.rgb(44,62,80));
                generateQuestion();

            }
        }.start();
    }

    public void showAnimation(){
        mAnimationDrawable = (AnimationDrawable) myLayout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(1000);
        mAnimationDrawable.setExitFadeDuration(2000);
        mAnimationDrawable.start();
//        mCardView1 =  findViewById(R.id.cardView1);
        mCardView1.setTranslationX(-1000f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(mCardView1, "translationX", 0f);
        animation.setDuration(500);
        animation.start();
//        mCardView2 =  findViewById(R.id.cardView2);
        mCardView2.setTranslationX(-1000f);
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(mCardView2, "translationX", 0f);
        animation1.setDuration(600);
        animation1.start();
        //mCardView3 =  findViewById(R.id.cardView3);
        mCardView3.setTranslationX(-1000f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mCardView3, "translationX", 0f);
        animation2.setDuration(800);
        animation2.start();
        //mCardView4 =  findViewById(R.id.cardView4);
        mCardView4.setTranslationX(-1000f);
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(mCardView4, "translationX", 0f);
        animation3.setDuration(1000);
        animation3.start();
        //mCardView5 = findViewById(R.id.cardView5);
        mCardView5.setTranslationX(-1000f);
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(mCardView5, "translationX", 0f);
        animation4.setDuration(1200);
        animation4.start();
    }

    public void showFinalScore(){
        startAgainTimer();
    }

    private void startAgainTimer() {
        new CountDownTimer(1000,100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finalScoreCardView.setVisibility(View.VISIBLE);
                finalScoreTextView.setText("Your Score: " + Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));

            }
        }.start();
    }
}
