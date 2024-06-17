package com.gudgo.jeju.domain.friends.entity;

import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isRequest;

    private boolean isApproval;

    private Long friendUserId;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    public Friends withIsApproval(boolean isApproval) {
        return Friends.builder()
                .id(this.id)
                .isRequest(this.isRequest)
                .isApproval(isApproval)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public Friends withIsDeleted(boolean isDeleted) {
        return Friends.builder()
                .id(this.id)
                .isRequest(this.isRequest)
                .isApproval(this.isApproval)
                .isDeleted(isDeleted)
                .user(this.user)
                .build();
    }
}
