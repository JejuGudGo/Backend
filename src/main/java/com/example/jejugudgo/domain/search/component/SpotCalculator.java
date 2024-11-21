package com.example.jejugudgo.domain.search.component;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.stereotype.Component;

@Component
public class SpotCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0; // 지구 반경 (단위: km)

    public BooleanExpression isAroundSpot(
            NumberPath<Double> spotLatitude, NumberPath<Double> spotLongitude, // entity 의 값
            String currentLatitude, String currentLongitude // front 에서의 값
    ) {
        try {
            double lat = Double.parseDouble(currentLatitude);
            double lon = Double.parseDouble(currentLongitude);
            return withinRadius(spotLatitude, spotLongitude, lat, lon);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("위경도 오류");
        }
    }
    private BooleanExpression withinRadius(
            NumberPath<Double> spotLatitude,
            NumberPath<Double> spotLongitude,
            double currentLatitude,
            double currentLongitude
    ) {
        Expression<Double> distance = Expressions.numberTemplate(Double.class,
                "({0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2}))))",
                EARTH_RADIUS_KM, spotLatitude, currentLatitude, spotLongitude, currentLongitude);

        return Expressions.booleanTemplate("{0} <= 10.0", distance);
    }
}
