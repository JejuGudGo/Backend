package com.example.jejugudgo.domain.user.athentication.term.entity;

import com.example.jejugudgo.global.data.term.entity.Term;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isAgreed;

    private String updatedAt;


    @ManyToOne()
    @JoinColumn(name = "termId")
    private Term term;


    public UserTerm updateIsAgreed(boolean isAgreed, String updatedAt) {
        return toBuilder()
                .isAgreed(isAgreed)
                .updatedAt(updatedAt)
                .build();
    }
}
