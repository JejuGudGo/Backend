package com.example.jejugudgo.domain.course.common.query;

import com.example.jejugudgo.domain.course.common.dto.response.TrailListResponse;
import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.repository.TrailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TrailQueryService {

    private final TrailRepository trailRepository; // 산책로 데이터 접근 레포지토리

    /**
     * 특정 트레일 ID를 기반으로 TrailListResponse를 반환하는 메서드
     *
     * @param targetId 트레일 ID
     * @return TrailListResponse
     */
    public TrailListResponse getTrailForList(Long targetId) {
        // Trail 데이터 조회
        Trail trail = trailRepository.findById(targetId).orElse(null);
        if (trail != null) {
            return new TrailListResponse(
                    trail.getId(), // 트레일 ID
                    trail.getTrailTag().getTag(), // 태그 정보
                    trail.getTitle(), // 트레일 제목
                    trail.getContent(), // 트레일 내용
                    trail.getThumbnailUrl(), // 썸네일 URL
                    trail.getStarAvg(), // 평균 별점
                    trail.getReviewCount() // 리뷰 수
            );
        }

        return null; // 조회된 데이터가 없으면 null 반환
    }
}

