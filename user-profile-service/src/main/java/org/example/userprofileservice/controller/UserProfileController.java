package org.example.userprofileservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userprofileservice.dto.UserProfileDto;
import org.example.userprofileservice.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable UUID userId) {
        return userProfileService.getProfile(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfileDto> createOrUpdateProfile(@RequestBody UserProfileDto userProfileDto) {
        UserProfileDto updated = userProfileService.createOrUpdateProfile(userProfileDto);
        return ResponseEntity.ok(updated);
    }
}
