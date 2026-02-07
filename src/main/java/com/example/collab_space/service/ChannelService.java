package com.example.collab_space.service;

import com.example.collab_space.model.*;
import com.example.collab_space.repository.*;
import com.example.collab_space.requestDto.AddChannelMemberDto;
import com.example.collab_space.requestDto.ChannelCreationDto;
import com.example.collab_space.requestDto.UserChannelReqDto;
import com.example.collab_space.responseDto.ChannelResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    @Autowired
    WorkspaceRepo workspaceRepo;

    @Autowired
    ChannelRepo channelRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkspaceMemberRepo workspaceMemberRepo;

    @Autowired
    ChannelMemberRepo channelMemberRepo;

    public void createChannel(String userEmail, ChannelCreationDto channelCreationDto) {
        Optional<Workspace> workspace = workspaceRepo.findById(channelCreationDto.getWorkspaceId());

        if(workspace.isEmpty()){
            throw new RuntimeException("workspace does not exists");
        }

        Channel channel1 = channelRepo.findByName(channelCreationDto.getChannelName().toLowerCase());

        if(channel1 != null){
            throw new RuntimeException("Channel with this name already exists");
        }

        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new RuntimeException("User does not exists");
        }

        List<WorkspaceMember> list = workspaceMemberRepo.findByWorkspace(workspace.get());
        boolean isMember = false;
        for(WorkspaceMember workspaceMember : list){
            if(workspaceMember.getUser() == user){
                isMember = true;
                break;
            }
        }

        if(!isMember){
            throw  new RuntimeException("User is not in our workspace");
        }

        Channel channel = new Channel();
        channel.setName(channelCreationDto.getChannelName().toLowerCase());
        channel.setPrivate(Boolean.parseBoolean(channelCreationDto.getIsPrivate().toLowerCase()));
        channel.setWorkspace(workspace.get());
        channel.setCreatedAt(LocalDate.now());
        channel.setUpdateAt(LocalDate.now());
        channelRepo.save(channel);

        if(Boolean.parseBoolean(channelCreationDto.getIsPrivate())){
            Channelmember channelmember = new Channelmember();
            channelmember.setChannel(channel);
            channelmember.setUser(user);
            channelmember.setJoinedAt(LocalDate.now());
            channelMemberRepo.save(channelmember);
        }else {
            for (WorkspaceMember workspaceMember : list) {
                Channelmember channelmember = new Channelmember();
                channelmember.setUser(workspaceMember.getUser());
                channelmember.setChannel(channel);
                channelmember.setJoinedAt(LocalDate.now());
                channelMemberRepo.save(channelmember);
            }
        }
    }


    public void addChannelMember(String userEmail, AddChannelMemberDto channelMemberDto) {
        Optional<Workspace> workspace = workspaceRepo.findById(channelMemberDto.getWorkspaceId());

        if(workspace.isEmpty()){
            throw new RuntimeException("workspace does not exists");
        }

        Optional<Channel> channel1 = channelRepo.findById(channelMemberDto.getChannelId());

        if(channel1.isEmpty()){
            throw new RuntimeException("Channel does not exists");
        }

        User user = userRepository.findByEmail(userEmail);
        Optional<User> member = userRepository.findById(channelMemberDto.getMemberId());
        if(user == null || member.isEmpty()){
            throw new RuntimeException("User does not exists");
        }

        List<WorkspaceMember> list = workspaceMemberRepo.findByWorkspace(workspace.get());
        boolean isUserExists = false;
        boolean isMemberExists = false;

        for(WorkspaceMember workspaceMember : list){
            if(isMemberExists && isUserExists){
                break;
            }
            if(workspaceMember.getUser() == user){
                isUserExists = true;
            }else if(workspaceMember.getUser() == member.get()){
                isMemberExists = true;
            }
        }

        if(!isMemberExists || !isUserExists){
            throw  new RuntimeException("User is not in our workspace");
        }

        isUserExists = false;
        isMemberExists = false;

        List<Channelmember> channelmembers = channelMemberRepo.findByChannel(channel1.get());
        for(Channelmember cm : channelmembers){
            if(isMemberExists && isUserExists){
                break;
            }
            if(cm.getUser() == user){
                isUserExists = true;
            }else if(cm.getUser() == member.get()){
                isMemberExists = true;
            }
        }

        if(isMemberExists){
            throw new RuntimeException("User already in the channel");
        } else if (!isUserExists) {
            throw new RuntimeException("Invalid invitation");
        }else {
            Channelmember channelmember = new Channelmember();
            channelmember.setChannel(channel1.get());
            channelmember.setUser(member.get());
            channelmember.setJoinedAt(LocalDate.now());
            channelMemberRepo.save(channelmember);
        }

    }

    public List<ChannelResponseDto> fetchUserChannel(UserChannelReqDto reqDto) {
        Optional<Workspace> workspace = workspaceRepo.findById(reqDto.getWorkspaceId());

        User user = userRepository.findByEmail(reqDto.getUserEmail());
        if(user == null || !user.isActive()){
            throw new RuntimeException("User does not exists or inactive account");
        }

        if(workspace.isEmpty()){
            throw new RuntimeException("workspace does not exists");
        }

        List<WorkspaceMember> list = workspaceMemberRepo.findByWorkspace(workspace.get());
        boolean isUserExists = false;

        for(WorkspaceMember workspaceMember : list){
            if(workspaceMember.getUser() == user){
                isUserExists = true;
                break;
            }
        }

        if(!isUserExists){
            throw  new RuntimeException("User is not in our workspace");
        }

        List<Channel> channels = channelRepo.findByWorkspace(workspace.get());
        List<ChannelResponseDto> responseDtos = new ArrayList<>();

        for(Channel channel : channels){
            List<Channelmember> channelMembers = channelMemberRepo.findByChannel(channel);
            for(Channelmember member : channelMembers){
                if(member.getUser() == user){
                    ChannelResponseDto responseDto = new ChannelResponseDto();
                    responseDto.setChannelId(channel.getId());
                    responseDto.setChannelName(channel.getName());
                    responseDto.setPrivate(channel.isPrivate());
                    responseDtos.add(responseDto);
                }
            }
        }

        return responseDtos;
    }
}
