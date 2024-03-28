package com.example.BE_PROJECT_OPEN_COLLAB.GroupChat;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepository extends CrudRepository<GroupChatEntity, Long>{

}
