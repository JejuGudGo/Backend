package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.repository.TrailRepository;
import com.example.jejugudgo.domain.course.search.dto.request.CourseRequest;
import com.example.jejugudgo.domain.course.search.dto.response.BasicData;
import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import com.example.jejugudgo.domain.course.search.dto.response.SimilarPoint;
import com.example.jejugudgo.domain.course.search.dto.response.TrailInfoData;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.util.UserLikeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailDetailService implements CourseDetailService {
    private final UserLikeUtil userLikeUtil;
    private final TrailRepository trailRepository;

    @Override
    public CourseDetailResponse getCourseDetail(HttpServletRequest httpRequest, CourseRequest request) {
        Object basicData = getBasicData(httpRequest, request);
        Object infoData = getInfoData(httpRequest, request);
        Object reviewData = getReviewData(httpRequest, request);

        return new CourseDetailResponse(
                basicData,
                infoData,
                null
        );
    }

    @Override
    public Object getBasicData(HttpServletRequest httpRequest, CourseRequest request) {
        Trail trail = getTrail(request);
        List<String> tags = new ArrayList<>();
        tags.add(trail.getTrailTag().getTag());

        LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1().getType(), request.id());

        return new BasicData(
                trail.getId(),
                tags,
                likeInfo,
                trail.getTitle(),
                null,
                null,
                trail.getTime(),
                trail.getStarAvg(),
                trail.getReviewCount()
        );
    }

    @Override
    public Object getInfoData(HttpServletRequest httpRequest, CourseRequest request) {
        Trail trail = getTrail(request);

        RoutePoint startPointInfo = new RoutePoint(
                trail.getTitle(),
                trail.getLatitude(),
                trail.getLongitude()
        );

        List<Trail> trails = trailRepository.findByTrailTag(trail.getTrailTag());
        List<SimilarPoint> similarPoints = trails.stream()
                .map(point -> new SimilarPoint(
                        point.getId(),
                        userLikeUtil.isLiked(httpRequest, request.cat1().getType(), request.id()),
                        point.getTitle(),
                        request.cat1().getType(),
                        point.getThumbnailUrl()
                ))
                .toList();

        return new TrailInfoData(
                trail.getContent(),
                startPointInfo,
                trail.getAddress(),
                trail.getOpenTime(),
                trail.getTel(),
                trail.getFee(),
                trail.getTime(),
                trail.getHomepage(),
                trail.getThumbnailUrl(),
                similarPoints
        );
    }

    @Override // TODO : 리뷰 기능 생성시 작성하기
    public Object getReviewData(HttpServletRequest httpRequest, CourseRequest request) {
        return null;
    }

    private Trail getTrail(CourseRequest request) {
        return trailRepository.findById(request.id())
                .orElse(null);
    }
}
