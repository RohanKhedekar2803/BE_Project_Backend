package com.example.BE_PROJECT_OPEN_COLLAB.Models;

import java.util.List;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteTopic;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String nickname;
    private List<FavouriteLanguage> favouriteLanguages;
    private List<FavouriteTopic> favouriteTopic;
    private Boolean isOrganization;
}
