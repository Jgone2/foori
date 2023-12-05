package com.foriserver.fori.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBERS")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = false, updatable = true, length = 20)
    private String name;

    @Column(nullable = false, unique = true, updatable = false, length = 15)
    private String loginId;

    @Column(nullable = false, unique = false, updatable = true)
    private String password;

    @Column(nullable = false, unique = true, updatable = true, length = 30)
    private String email;
}
