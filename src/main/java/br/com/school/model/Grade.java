package br.com.school.model;

public class Grade {
    private Double score;

    public Grade() { }

    public Grade(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
