package com.example.collab_space.repository;

import com.example.collab_space.model.Channelmember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepo extends JpaRepository<Channelmember,Long> {
}
