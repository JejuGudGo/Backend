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

    private String nickname;

    private String numberTag;

    private String profileImageUrl;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    public void setIsApproval(boolean isApproval) {
        this.isApproval = isApproval;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
