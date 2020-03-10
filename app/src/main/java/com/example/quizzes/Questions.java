package com.example.quizzes;

public class Questions {
    String questions;
    String option1;
    String option2;
    String option3;
    String option4;
    String answer;
    String imageUrl;

    public Questions(String questions, String option1, String option2, String option3, String option4, String answer, String imageUrl) {
        this.questions = questions;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.imageUrl = imageUrl;
    }

    public String getQuestions() {
        return questions;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
