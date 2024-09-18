package com.gudgo.jeju.domain.bookmark.query;

import com.gudgo.jeju.domain.bookmark.dto.request.FilterDto;
import com.gudgo.jeju.domain.bookmark.entity.BookMark;
import com.gudgo.jeju.domain.bookmark.entity.QBookMark;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerTagRepository;
import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.trail.dto.response.TrailListResponse;
import com.gudgo.jeju.domain.trail.entity.QTrail;
import com.gudgo.jeju.domain.trail.entity.QTrailTag;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.trail.entity.TrailType;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkQueryService {

    private final JPAQueryFactory queryFactory;
    private final PlannerTagRepository plannerTagRepository;
    private final ReviewQueryService reviewQueryService;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;

    @Autowired
    public BookmarkQueryService(EntityManager entityManager, PlannerTagRepository plannerTagRepository, ReviewQueryService reviewQueryService, JeJuOlleCourseRepository jeJuOlleCourseRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.plannerTagRepository = plannerTagRepository;
        this.reviewQueryService = reviewQueryService;
        this.jeJuOlleCourseRepository = jeJuOlleCourseRepository;
    }

    public Page<?> get(Long userId, FilterDto filter, Pageable pageable) {
        QBookMark bookMark = QBookMark.bookMark;
        QPlanner planner = QPlanner.planner;
        QCourse course = QCourse.course;
        QTrail trail = QTrail.trail;
        QTrailTag trailTag = QTrailTag.trailTag1;
        QReview review = QReview.review;

        List<BookMark> bookMarks = queryFactory
                .selectFrom(bookMark)
                .leftJoin(bookMark.planner, planner).fetchJoin()
                .leftJoin(planner.course, course).fetchJoin()
                .leftJoin(bookMark.trail, trail).fetchJoin()
                .leftJoin(trailTag).on(trailTag.trail.eq(trail))
                .where(
                        bookMark.user.id.eq(userId),
                        bookMark.isDeleted.isFalse(),
                        typeFilter(filter)
                )
                .groupBy(bookMark.id)
                .orderBy(bookMark.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<?> responses = bookMarks.stream()
                .map(bm -> convertToDto(bm, review))
                .collect(Collectors.toList());

        return PaginationUtil.listToPage(responses, pageable);
    }

    private BooleanExpression typeFilter(FilterDto filter) {
        QBookMark bookMark = QBookMark.bookMark;

        if (filter.courseType() != null) {
            if (filter.courseType() == CourseType.OLLE) {
                return bookMark.planner.course.type.in(CourseType.JEJU, CourseType.HAYOUNG);
            } else if (filter.courseType() == CourseType.USER) {
                return bookMark.planner.course.type.eq(CourseType.USER);
            }
        } else if (filter.trailType() != null) {
            // 트레일 타입이 선택된 경우, 모든 트레일 북마크를 반환
            return bookMark.trail.isNotNull();
        }

        return null;
    }

    private Object convertToDto(BookMark bookMark, QReview review) {
        if (bookMark.getPlanner() != null) {
            return convertPlannerBookmarkToDto(bookMark);
        } else if (bookMark.getTrail() != null) {
            return convertTrailBookmarkToDto(bookMark, review);
        }
        throw new IllegalStateException("BookMark must have either a Planner or a Trail");
    }

    private PlannerListResponse convertPlannerBookmarkToDto(BookMark bookMark) {
        Planner planner = bookMark.getPlanner();

        String distance = getDistance(planner.getCourse());
        List<String> tagResponses = plannerTagRepository.findByPlanner(planner).stream()
                .map(tag -> tag.getCode().toString())
                .collect(Collectors.toList());

        Course course = planner.getCourse();

        Long reviewCount = reviewQueryService.getUserCourseReviewCount(planner.getId());

        return new PlannerListResponse(
                planner.getId(),
                course.getContent(),
                distance,
                planner.getTime(),
                course.getStarAvg(),
                reviewCount,
                planner.isCompleted(),
                planner.isPrivate(),
                tagResponses
        );
    }

    private TrailListResponse convertTrailBookmarkToDto(BookMark bookMark, QReview review) {
        Trail trail = bookMark.getTrail();
        QTrailTag trailTag = QTrailTag.trailTag1;

        Double avgStars = queryFactory
                .select(review.stars.avg())
                .from(review)
                .where(review.trail.eq(trail))
                .fetchOne();

        Long reviewCount = reviewQueryService.getTrailReviewCount(trail.getId());


        List<TrailType> tags = queryFactory
                .select(trailTag.trailTag)
                .from(trailTag)
                .where(trailTag.trail.eq(trail))
                .fetch();

        return new TrailListResponse(
                trail.getTitle(),
                trail.getSummary(),
                avgStars,
                reviewCount,
                tags
        );
    }
    private String getDistance(Course course) {
        if (course.getType().equals(CourseType.JEJU) || course.getType().equals(CourseType.HAYOUNG)) {
            JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(course.getId())
                    .orElseThrow(EntityNotFoundException::new);
            return jeJuOlleCourse.getTotalDistance();
        }
        return null;
    }
}