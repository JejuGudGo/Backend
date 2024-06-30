package com.gudgo.jeju.domain.planner.service;

import com.gudgo.jeju.domain.planner.dto.request.chatting.NoticeCreateRequest;
import com.gudgo.jeju.domain.planner.dto.request.chatting.NoticeUpdateRequest;
import com.gudgo.jeju.domain.planner.dto.response.NoticeResponse;
import com.gudgo.jeju.domain.planner.entity.ChatRoom;
import com.gudgo.jeju.domain.planner.entity.Notice;
import com.gudgo.jeju.domain.planner.entity.Participant;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.query.NoticeQueryService;
import com.gudgo.jeju.domain.planner.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.repository.ChatRoomRepository;
import com.gudgo.jeju.domain.planner.repository.NoticeRepository;
import com.gudgo.jeju.domain.planner.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final NoticeQueryService noticeQueryService;
    private final ParticipantQueryService participantQueryService;
    private final PlannerRepository plannerRepository;
    private final ParticipantRepository participantRepository;

    public List<NoticeResponse> getNotices(Long chatRoomId) {
        return noticeQueryService.getNotices(chatRoomId);
    }

    public NoticeResponse getLatestNotice(Long chatRoomId) {
        return noticeQueryService.getLatestNotice(chatRoomId);
    }

    public NoticeResponse create(Long userId, Long chatRoomId, NoticeCreateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(EntityNotFoundException::new);

        Planner planner = plannerRepository.findByChatRoomId(chatRoom.getId());

        Participant participant = participantRepository.findByUserIdAndPlannerId(userId, planner.getId())
                .orElseThrow(EntityNotFoundException::new);

        Notice notice = Notice.builder()
                .chatRoom(chatRoom)
                .participant(participant)
                .content(request.content())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);

        NoticeResponse response = new NoticeResponse(
                notice.getId(),
                notice.getParticipant().getId(),
                notice.getParticipant().getUser().getNickname(),
                notice.getParticipant().getUser().getNumberTag(),
                notice.getParticipant().getUser().getProfile().getProfileImageUrl(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );

        return response;
    }

    public NoticeResponse update(Long noticeId, NoticeUpdateRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(EntityNotFoundException::new);

        notice = notice.withContent(request.content());
        noticeRepository.save(notice);

        NoticeResponse response = new NoticeResponse(
                notice.getId(),
                notice.getParticipant().getId(),
                notice.getParticipant().getUser().getNickname(),
                notice.getParticipant().getUser().getNumberTag(),
                notice.getParticipant().getUser().getProfile().getProfileImageUrl(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );

        return response;
    }

    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(EntityNotFoundException::new);

        notice = notice.withIsDeleted();

        noticeRepository.save(notice);
    }
}
