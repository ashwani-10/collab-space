package com.example.collab_space.repository;

import com.example.collab_space.model.Channel;
import com.example.collab_space.model.Channelmember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMemberRepo extends JpaRepository<Channelmember,Long> {
    List<Channelmember> findByChannel(Channel channel);
}
