package com.csye6225.piggymemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long user;

    @ColumnDefault("'https://hlgvugiuwrwkdvvtisms.supabase.co/storage/v1/object/public/public_assets/piggy-bank.svg'")
    @Column(name = "avatar_url", length = Integer.MAX_VALUE)
    private String avatarUrl;

    @NotNull
    @ColumnDefault("'New user'")
    @Column(name = "nickname", nullable = false, length = Integer.MAX_VALUE)
    private String nickname;

    @Column(name = "bio", length = Integer.MAX_VALUE)
    private String bio;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_profile_public", nullable = false)
    private Boolean isProfilePublic;

    @Column(name = "family_id")
    private Long family;

    public Long getId() {
        return id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getIsProfilePublic() {
        return isProfilePublic;
    }

    public void setIsProfilePublic(Boolean isProfilePublic) {
        this.isProfilePublic = isProfilePublic;
    }

    public Long getFamily() {
        return family;
    }

    public void setFamily(Long family) {
        this.family = family;
    }

}
