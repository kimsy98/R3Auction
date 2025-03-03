package com.r3a.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RatingScoreTest {
    @Test
    public void 평가점수생성자(){
        RatingScore rc = new RatingScore();
        Assertions.assertThat(rc).isEqualTo(new RatingScore());
    }
    @Test
    public void 평가점수계산로직테스트(){
        int bidCnt = 3;
        RatingScore rc = new RatingScore();
        rc.calcScore();
        Assertions.assertThat(rc.getScore()).isEqualTo(48.50);
        bidCnt = 0;
        rc.calcScore();
        Assertions.assertThat(rc.getScore()).isEqualTo(49.00);
    }

    @Test
    public void 거래횟수초기화(){
        RatingScore ratingScore = new RatingScore(3);

        ratingScore.resetTransactionCount();
        Assertions.assertThat(ratingScore.getTransactionCnt()).isEqualTo(0);

    }
}
