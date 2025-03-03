package com.r3a.user;

import java.util.Objects;

public class RatingScore {
    private double score;
    private int transactionCnt;
    public RatingScore() {

        this.score = 50.00;
        this.transactionCnt = 0;
    }

    public RatingScore(int transactionCnt) {
        this.transactionCnt = transactionCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingScore that)) return false;
        return Double.compare(that.score, score) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }

    public double getScore() {
        return score;
    }

    public int getTransactionCnt() {
        return transactionCnt;
    }

    public double calcScore() {
        if(transactionCnt>0){
            score -=0.5*transactionCnt;
            return score;
        }
        score+=0.5;
        return score;
    }

    public void resetTransactionCount() {
        this.transactionCnt = 0;
    }
}
