package com.example.jejugudgo.global.api.tourapi.area.service;

import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiContentType;
import com.example.jejugudgo.global.api.tourapi.area.repository.TourApiContentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourApiService {
    private final TourApiContentTypeRepository tourApiContentTypeRepository;

    public void loadTourApiContentTypeIds() {
        List<ContentType> contentTypes = ContentType.getAllContentTypeIds();

        for (ContentType contentType : contentTypes) {
            if (tourApiContentTypeRepository.findByContentType(contentType) == null) {
                TourApiContentType tourApiContentType = TourApiContentType.builder()
                        .contentType(contentType)
                        .build();

                tourApiContentTypeRepository.save(tourApiContentType);
            }
        }
    }
}
