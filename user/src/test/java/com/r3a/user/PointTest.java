package com.r3a.user;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PointTest {
    @Test
    public void 포인트생성자(){
        Point point = new Point(0);

        assertThat(point).isEqualTo(new Point(0));
    }
    @Test
    public void 포인트유효성검증(){
        Point point = new Point(0);
        point.addPoint(-100);
        assertThat(point.getPoint()).isEqualTo(0);
    }
}
