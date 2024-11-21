package com.example.jejugudgo.domain.course.olle.docs;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class JejuOlleSpotDocument {
    @Id
    private Long spotId;

    private String title;

    private double latitude;

    private double longitude;

    private Long spotOrder;


    public static JejuOlleSpotDocument of (JejuOlleSpot jejuOlleSpot) {
        JejuOlleSpotDocument document = new JejuOlleSpotDocument();
        document.setSpotId(jejuOlleSpot.getId());
        document.setTitle(jejuOlleSpot.getTitle());
        document.setLatitude(jejuOlleSpot.getLatitude());
        document.setLongitude(jejuOlleSpot.getLongitude());
        document.setSpotOrder(jejuOlleSpot.getSpotOrder());

        return document;
    }
}
