package com.senla.pricemonitor.service;

import com.senla.pricemonitor.dto.UserProfileDto;
import com.senla.pricemonitor.entity.User;
import com.senla.pricemonitor.exception.NotFoundException;
import com.senla.pricemonitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        var profile = user.getProfile();

        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setName(profile.getName());
        profileDto.setAvatarUrl(profile.getAvatarUrl());

        return profileDto;
    }

    @Transactional
    public UserProfileDto update(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        var profile = user.getProfile();

        profile.setName(profileDto.getName());
        profile.setAvatarUrl(profileDto.getAvatarUrl());

        userRepository.save(user);

        return profileDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}