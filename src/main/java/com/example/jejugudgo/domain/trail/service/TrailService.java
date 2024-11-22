package com.example.jejugudgo.domain.trail.service;

import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.message.TrailPublisher;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;
    private final TrailPublisher publisher;

    public void updateStarAvg(double newStarAvg, Trail trail) {
        trail = trail.updateStarAvg(newStarAvg);
        trailRepository.save(trail);
        publisher.updateTrailMessagePublish(trail, newStarAvg);
    }
}
