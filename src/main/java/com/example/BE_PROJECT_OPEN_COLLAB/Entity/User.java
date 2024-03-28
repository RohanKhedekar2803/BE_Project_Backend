package com.example.BE_PROJECT_OPEN_COLLAB.Entity;

import java.util.ArrayList;
import java.util.List;

import com.example.BE_PROJECT_OPEN_COLLAB.GroupChat.GroupChatEntity;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.Status;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Entity
public class User {
	
	@Id
    private String username;
    private String password;
    private String nickname;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<FavouriteLanguage> favouriteLanguages;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<FavouriteTopic> favouriteTopic;
    
    
    private Status status;
    
    @Builder.Default
    private Boolean isOrganization=false;
    
    @ManyToMany(mappedBy = "members")
    private ArrayList<GroupChatEntity> groupChatsUserAssociatedWith;

}
