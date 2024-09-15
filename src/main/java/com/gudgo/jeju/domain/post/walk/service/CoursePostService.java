package com.gudgo.jeju.domain.post.walk.service;

import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleSpotRepository;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.post.chat.repository.ChatRoomRepository;
import com.gudgo.jeju.domain.post.participant.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostCreateResponse;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostSpotResponse;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePostService {
    private final PostsRepository postsRepository;
    private final CourseRepository courseRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final JeJuOlleSpotRepository jeJuOlleSpotRepository;
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final ParticipantQueryService participantQueryService;
    private final PlannerRepository plannerRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PlannerCopyService plannerCopyService;


    private final ValidationUtil validationUtil;

    @Transactional
    public CoursePostCreateResponse createCoursePost(Long userId, CoursePostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        ChatRoom chatRoom = ChatRoom.builder()
                .title(request.title())
                .build();

        chatRoomRepository.save(chatRoom);

        Planner copiedPlanner = plannerCopyService.copyPlanner(chatRoom, user, request.selectedPlannerId());

        Posts posts = Posts.builder()
                .user(user)
                .planner(copiedPlanner)
                .postType(PostType.COURSE)
                .title(request.title())
                .startDate(request.startDate())
                .startAt(request.startTime())
                .companionsNum(request.companionsNum())
                .placeName(request.placeName())
                .placeLatitude(request.placeLatitude())
                .placeLonggitude(request.placeLongitude())
                .build();

        postsRepository.save(posts);

        return getCreateCoursePostResponse(user, posts);
    }

    private CoursePostCreateResponse getCreateCoursePostResponse(User user, Posts posts) {
        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderNumberAsc(posts.getPlanner().getCourse().getId());
        List<CoursePostSpotResponse> spotResponses = new ArrayList<>();

        for (Spot spot : spots) {
            spotResponses.add(new CoursePostSpotResponse(spot.getTitle(), spot.getOrderNumber(), spot.getLatitude(), spot.getLongitude()));
        }

        return new CoursePostCreateResponse(
                posts.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                posts.getCreatedAt(),
                posts.getStartDate(),
                posts.getStartAt(),
                posts.getCompanionsNum(),
                posts.getPlanner().getTime(),
                posts.getPlaceName(),
                posts.getContent(),
                spotResponses
        );
    }

//    public CoursePostResponse getCoursePost(Long postId) {
//        Posts post = postsRepository.findById(postId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Long currentParticipantNum = participantQueryService.countCourseParticipant(post.getPlanner().getId());
//
//        return new CoursePostResponse(
//                post.getId(),
//                post.getUser().getId(),
//                post.getUser().getNickname(),
//                post.getUser().getProfile().getProfileImageUrl(),
//                post.getUser().getNumberTag(),
//                post.getTitle(),
//                post.getCompanionsNum(),
//                currentParticipantNum,
//                post.getContent()
//        );
//    }
//
//    @Transactional
//    public CoursePostResponse createByUserCourse(CoursePostCreateRequest request) {
//        /* 1. Course 테이블에 카피 진행 */
//        // 인용한 course 가 있는 플래너
//        Planner planner = plannerRepository.findById(request.plannerId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        Course copyCourse = Course.builder()
//                .type(planner.getCourse().getType())
//                .title(planner.getCourse().getTitle())
//                .createdAt(planner.getCourse().getCreatedAt())
//                .originalCreatorId(planner.getCourse().getOriginalCreatorId())
//                .originalCourseId(planner.getCourse().getOriginalCourseId())
//                .build();
//
//        courseRepository.save(copyCourse);
//
//        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderNumberAsc(planner.getCourse().getId());
//        for (Spot spot : spots) {
//            Spot copySpot = Spot.builder()
//                    .course(copyCourse)
//                    .spotType(spot.getSpotType())
//                    .orderNumber(spot.getOrderNumber())
//                    .title(spot.getTitle())
//                    .address(spot.getAddress())
//                    .latitude(spot.getLatitude())
//                    .longitude(spot.getLongitude())
//                    .isDeleted(false)
//                    .isCompleted(false)
//                    .count(0L)
//                    .contentId(spot.getContentId())
//                    .build();
//
//            spot = spot.withIncreasedCount();
//            spotRepository.save(spot);
//            spotRepository.save(copySpot);
//        }
//
//        /* 2. Planner 테이블에 필드 생성 */
//        User user = userRepository.findById(request.userId())
//                .orElseThrow(EntityNotFoundException::new);
//
//
//        /* ChatRoom 생성 */
//        ChatRoom chatRoom = ChatRoom.builder()
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//
//        Planner newPlanner = Planner.builder()
//                .chatRoom(chatRoom)
//                .user(user)
//                .course(copyCourse)
//                .startAt(LocalDate.now())
//                .isDeleted(false)
//                .isPrivate(false)
////                .summary()
////                .time()
//                .isCompleted(false)
//                .build();
//
//        plannerRepository.save(newPlanner);
//
//        /* 3. 게시글을 올린 user 를 코스 참가자로 등록 */
//        Participant participant = Participant.builder()
//                .user(user)
//                .planner(newPlanner)
//                .approved(true)
//                .isDeleted(false)
//                .build();
//
//        participantRepository.save(participant);
//
//        /* 4. Posts 테이블에 저장*/
//        Posts posts = Posts.builder()
//                .user(user)
//                .postType(PostType.COURSE)
//                .planner(newPlanner)
//                .title(request.title())
//                .content(request.content())
//                .companionsNum(request.companionsNum())
//                .createdAt(LocalDate.now())
//                .isFinished(false)
//                .isDeleted(false)
//                .placeName(request.placeName())
//                .placeLatitude(request.placeLatitude())
//                .placeLonggitude(request.placeLongitude())
//                .build();
//
//
//        postsRepository.save(posts);
//
//        return getResponse(posts, copyCourse.getId());
//    }
//
//    @Transactional
//    public CoursePostResponse createByOlle(CoursePostCreateRequest request) {
//        /* 1. Course 테이블에 카피 진행 */
//        // 인용한 course 가 있는 플래너
//        JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(request.olleCourseId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        Course copyCourse = Course.builder()
//                .type(CourseType.JEJU)
//                .title(jeJuOlleCourse.getTitle())
//                .olleCourseId(jeJuOlleCourse.getId())
//                .build();
//
//        courseRepository.save(copyCourse);
//
//        List<JeJuOlleSpot> spots = jeJuOlleSpotRepository.findAllByJeJuOlleCourse_id(request.olleCourseId());
//        for (JeJuOlleSpot spot : spots) {
//            Spot copySpot = Spot.builder()
//                    .course(copyCourse)
//                    .orderNumber(spot.getOrderNumber())
//                    .title(spot.getTitle())
//                    .latitude(spot.getLatitude())
//                    .longitude(spot.getLongitude())
//                    .isDeleted(false)
//                    .isCompleted(false)
//                    .count(0L)
//                    .build();
//
//            spotRepository.save(copySpot);
//        }
//
//        /* 2. Planner 테이블에 필드 생성 */
//        User user = userRepository.findById(request.userId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        /* ChatRoom 생성 */
//        ChatRoom chatRoom = ChatRoom.builder()
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//
//        Planner newPlanner = Planner.builder()
//                .user(user)
//                .course(copyCourse)
//                .chatRoom(chatRoom)
//                .startAt(LocalDate.now())
//                .isDeleted(false)
//                .isPrivate(true)
////                .summary()
////                .time()
//                .isCompleted(false)
//                .build();
//
//        plannerRepository.save(newPlanner);
//
//        /* 3. 게시글을 올린 user 를 코스 참가자로 등록 */
//        Participant participant = Participant.builder()
//                .user(user)
//                .planner(newPlanner)
//                .approved(true)
//                .isDeleted(false)
//                .build();
//
//        participantRepository.save(participant);
//
//        /* 4. Posts 테이블에 저장*/
//        Posts posts = Posts.builder()
//                .user(user)
//                .postType(PostType.COURSE)
//                .planner(newPlanner)
//                .title(request.title())
//                .content(request.content())
//                .companionsNum(request.companionsNum())
//                .createdAt(LocalDate.now())
//                .isFinished(false)
//                .isDeleted(false)
//                .placeName(request.placeName())
//                .placeLatitude(request.placeLatitude())
//                .placeLonggitude(request.placeLongitude())
//                .build();
//
//
//        postsRepository.save(posts);
//
//        return getResponse(posts, copyCourse.getId());
//    }
//
//
//    @Transactional
//    public CoursePostResponse update(Long postId, CoursePostUpdateRequest request) {
//        Posts post = postsRepository.findById(postId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        if (validationUtil.validateStringValue(request.title())) {
//            post = post.withTitle(request.title());
//
//        }
//
//        if (validationUtil.validateStringValue(request.content())) {
//            post = post.withContent(request.content());
//
//        }
//
//        if (validationUtil.validateLongValue(request.participantNum())) {
//            post = post.withCompanionsNum(request.participantNum());
//        }
//
//        if (request.isFinished() != post.isFinished()) {
//            post = post.withIsFinished(request.isFinished());
//        }
//
//        if (validationUtil.validateLongValue(request.plannerId())) {
//            Planner planner = plannerRepository.findById(request.plannerId())
//                    .orElseThrow(EntityNotFoundException::new);
//
//            post = post.withPlanner(planner);
//        }
//
//        postsRepository.save(post);
//
//        return getResponse(post, request.plannerId());
//    }
//
//
//    @Transactional
//    public void delete(Long postId) {
//        Posts posts = postsRepository.findById(postId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Posts updatedPosts = posts.withIsFinishedAndIsDeleted(true, true);
//        postsRepository.save(updatedPosts);
//    }
//
//    private CoursePostResponse getResponse(Posts post, Long plannerId) {
//        CoursePostResponse coursePostResponse = new CoursePostResponse(
//                post.getId(),
//                post.getUser().getId(),
//                post.getUser().getNickname(),
//                post.getUser().getProfile().getProfileImageUrl(),
//                post.getUser().getNumberTag(),
//                post.getTitle(),
//                post.getCompanionsNum(),
//                participantQueryService.countCourseParticipant(plannerId),
//                post.getContent()
//        );
//
//        return coursePostResponse;
//    }
}
