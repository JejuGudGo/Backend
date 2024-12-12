package com.example.jejugudgo.global.util.coordinate;

import org.springframework.stereotype.Component;

import java.lang.Math;

@Component
public class CoordinateConverter {
    // 타원체 상수 (GRS80)
    private static final double EARTH_RADIUS = 6378137.0; // 지구 장축 반경 (미터)
    private static final double FLATTENING = 1 / 298.257222101; // 편평률
    private static final double SECOND_ECCENTRICITY = Math.sqrt((2 * FLATTENING) - (FLATTENING * FLATTENING));

    // 원점 좌표 (서울)
    private static final double BASE_LAT = 38.0; // 기준 위도
    private static final double BASE_LON = 127.0; // 기준 경도
    private static final double FALSE_EASTING = 500000.0; // x 오프셋
    private static final double FALSE_NORTHING = 1000000.0; // y 오프셋

    /**
     * 위도, 경도를 EPSG:5181 (TM) X, Y 좌표로 변환
     *
     * @param lon 경도 (동경)
     * @param lat 위도 (북위)
     * @return double[] X, Y 좌표 배열 [x, y]
     */
    public static double[] convertToTMGrid(double lon, double lat) {
        // 1. 위도, 경도를 라디안으로 변환
        double lonRad = Math.toRadians(lon);
        double latRad = Math.toRadians(lat);
        double baseLatRad = Math.toRadians(BASE_LAT);
        double baseLonRad = Math.toRadians(BASE_LON);

        // 2. 이심률 계산
        double e = SECOND_ECCENTRICITY;
        double e1 = (1 - Math.sqrt(1 - e * e)) / (1 + Math.sqrt(1 - e * e));

        // 3. 위도 보정
        double v = EARTH_RADIUS / Math.sqrt(1 - Math.pow(e * Math.sin(latRad), 2));
        double ro = v * (1 - Math.pow(e, 2)) / (1 - Math.pow(e * Math.sin(latRad), 2));
        double n = EARTH_RADIUS / Math.sqrt(1 - Math.pow(e * Math.sin(latRad), 2));

        // 4. 위도 수렴 계산
        double t = Math.tan(latRad);
        double t2 = t * t;
        double n2 = v * v / ro / ro;

        // 5. 경위도 차이 계산
        double dLat = latRad - baseLatRad;
        double dLon = lonRad - baseLonRad;

        // 6. 좌표 계산
        double x = FALSE_EASTING +
                (n * Math.sin(latRad) * dLon * Math.cos(latRad)) +
                (n * Math.sin(latRad) * Math.pow(Math.cos(latRad), 3) *
                        (1 - t2 + n2) * Math.pow(dLon, 3) / 6);

        double y = FALSE_NORTHING +
                (ro * dLat) +
                (n * Math.tan(latRad) * Math.pow(dLon, 2) / 2) +
                (n * Math.tan(latRad) *
                        (5 - t2 + 9 * n2 + 4 * n2 * n2) *
                        Math.pow(dLon, 4) / 24);

        return new double[]{x, y};
    }

    /**
     * X 좌표 추출
     *
     * @param lon 경도
     * @param lat 위도
     * @return X 좌표
     */
    public static double convertToX(double lon, double lat) {
        return convertToTMGrid(lon, lat)[0];
    }

    /**
     * Y 좌표 추출
     *
     * @param lon 경도
     * @param lat 위도
     * @return Y 좌표
     */
    public static double convertToY(double lon, double lat) {
        return convertToTMGrid(lon, lat)[1];
    }
}
