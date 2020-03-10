package com.example.quizzes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView question;
    Button option1,option2,option3,option4;
    int correctPostion;
    String correctAnswer;
    TextView correct;
    TextView timer;
    TextView highScore;
    int totalQuestionAsked,noOfCorrectAnswer;
    int highScore1;
    CountDownTimer countDownTimer;
    int flag=0;
    Button playAgain;
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
        questionsList = new ArrayList<>();
        totalQuestionAsked=0;
        noOfCorrectAnswer=0;
        playAgain = findViewById(R.id.playAgain);
        preferences = getSharedPreferences(pref,MODE_PRIVATE);
        String x = preferences.getString("highScore","0");;
        highScore1 = Integer.parseInt(x);
        highScore.setText(x);
        reference = FirebaseDatabase.getInstance().getReference().child("questions");
        feedArrayList();
        Log.i("TAG",Integer.toString(questionsList.size()));

        flag=1;
        // generateQuestion();

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfCorrectAnswer=0;
                totalQuestionAsked=0;
                generateQuestion();
                option1.setEnabled(true);
                option3.setEnabled(true);
                option2.setEnabled(true);
                option4.setEnabled(true);
                correct.setText("0/0");
            }
        });



        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                checkAnswer(tag);
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
        startTimer();

    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                timer.setText("done!");
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                option4.setEnabled(false);
                correct.setText(Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
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

    public void checkAnswer(String tag){
        countDownTimer.cancel();
        if(tag.equals(Integer.toString(correctPostion))){
            showToast("Correct");
            noOfCorrectAnswer++;
            if(highScore1<noOfCorrectAnswer){
                highScore1=noOfCorrectAnswer;
            }
            saveHighScore();
            correct.setText(Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
            highScore.setText(Integer.toString(highScore1));
            generateQuestion();
        }
        else{
            showToast("Incorrect! correct answer is " + correctAnswer);
            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
            option4.setEnabled(false);
            countDownTimer.cancel();
            correct.setText(Integer.toString(noOfCorrectAnswer) + "/" + Integer.toString(totalQuestionAsked));
            highScore.setText(Integer.toString(highScore1));
        }
    }
}
