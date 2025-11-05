package com.dvc.RtLogistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @Column(name="user_id")
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name="userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<UserRole> roles = new HashSet<>();

}