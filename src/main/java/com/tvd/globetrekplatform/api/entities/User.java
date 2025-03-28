package com.tvd.globetrekplatform.api.entities;

import com.tvd.globetrekplatform.api.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Builder
@Setter
@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "fullname", length = 150)
    String fullName;

    @Column(name = "email", unique = true, length = 255)
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "password", length = 150, nullable = false)
    String password;

    @Column(name = "title",  columnDefinition = "TINYTEXT")
    String title;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "linkedin_url", length = 255)
    String linkedinUrl;

    @Column(name = "facebook_url", length = 255)
    String facebookUrl;

    @Column(name = "verification_code", length = 64)
    String verificationCode;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "profile_image", length = 255)
    String profileImage;

    @Column(name = "auth_type")
    @Enumerated(EnumType.STRING)
    AuthProvider authType;

    @Column(name = "google_account_id", unique = true)
    String googleAccountId;

    @Column(name = "github_account_id", unique = true)
    String appleAccountId;

    @Override
    public String getName() {
        return email != null ? email : (phoneNumber != null ? phoneNumber : null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}
