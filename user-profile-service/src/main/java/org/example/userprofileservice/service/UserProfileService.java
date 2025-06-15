package org.example.userprofileservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userprofileservice.dto.UserProfileDto;
import org.example.userprofileservice.entity.UserProfile;
import org.example.userprofileservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public Optional<UserProfileDto> getProfile(UUID userId) {
        return userProfileRepository.findByUserId(userId).map(this::mapToDto);
    }

    public UserProfileDto createOrUpdateProfile(UserProfileDto userProfileDto) {
        UserProfile profile = userProfileRepository.findByUserId(userProfileDto.getUserId())
                .orElse(UserProfile.builder().userId(userProfileDto.getUserId()).build());

        profile.setFirstName(userProfileDto.getFirstName());
        profile.setLastName(userProfileDto.getLastName());
        profile.setAvatarUrl(userProfileDto.getAvatarUrl());
        profile.setBio(userProfileDto.getBio());

        UserProfile saved = userProfileRepository.save(profile);
        return mapToDto(saved);
    }

    private UserProfileDto mapToDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .userId(userProfile.getUserId())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .avatarUrl(userProfile.getAvatarUrl())
                .bio(userProfile.getBio())
                .build();
    }
}
