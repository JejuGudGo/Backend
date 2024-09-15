package com.gudgo.jeju.domain.post.walk.service;

import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleSpotRepository;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.post.chat.repository.ChatRoomRepository;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.post.participant.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostDetailResponse;
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


    public CoursePostDetailResponse getCoursePost(Long userId, Long postId) {
        User user = findUser(userId);
        
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Long currentParticipantNum = participantQueryService.countCourseParticipant(post.getPlanner().getId());

        return getCreateCoursePostResponse(user, post, currentParticipantNum);
    }

    @Transactional
    public CoursePostDetailResponse createCoursePost(Long userId, CoursePostCreateRequest request) {
        User user = findUser(userId);

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
                .courseSummary(copiedPlanner.getCourse().getContent())
                .startDate(request.startDate())
                .startAt(request.startTime())
                .participantNum(request.participantNum())
                .placeName(request.placeName())
                .placeLatitude(request.placeLatitude())
                .placeLongitude(request.placeLongitude())
                .build();

        postsRepository.save(posts);

        setCoursePostHost(user, posts.getPlanner());

        return getCreateCoursePostResponse(user, posts, 0L);
    }

    private User findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
        
        return user;
    }
    
    private CoursePostDetailResponse getCreateCoursePostResponse(User user, Posts posts, Long currentParticipantNum) {
        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderNumberAsc(posts.getPlanner().getCourse().getId());
        List<CoursePostSpotResponse> spotResponses = new ArrayList<>();

        for (Spot spot : spots) {
            spotResponses.add(new CoursePostSpotResponse(spot.getTitle(), spot.getOrderNumber(), spot.getLatitude(), spot.getLongitude()));
        }

        return new CoursePostDetailResponse(
                posts.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                posts.getPlanner().getCourse().getContent(),
                posts.getCreatedAt(),
                posts.getStartDate(),
                posts.getStartAt(),
                currentParticipantNum,
                posts.getParticipantNum(),
                posts.getPlanner().getTime(),
                posts.getPlaceName(),
                posts.getContent(),
                spotResponses
        );
    }

    private void setCoursePostHost(User user, Planner planner) {
        Participant participant = Participant.builder()
                .user(user)
                .planner(planner)
                .isHost(true)
                .build();

        participantRepository.save(participant);
    }

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
