package com.r3a.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
@NoArgsConstructor
@Embeddable
public class Point {
    int point;

    public Point(int point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point1)) return false;
        return point == point1.point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    public void addPoint(int point) {
        if(point<=0) return;
        this.point += point;

    }

    public int getPoint() {
        return point;
    }
}
