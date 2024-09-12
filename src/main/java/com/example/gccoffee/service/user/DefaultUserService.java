package com.example.gccoffee.service.user;

import com.example.gccoffee.auth.MyJwtTokenProvider;
import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.user.Password;
import com.example.gccoffee.model.user.User;
import com.example.gccoffee.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final MyJwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User readDetail(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoDataException("해당하는 유저가 없습니다!"));
    }

    @Override
    public User signUp(User user) {
        return userRepository.save(user);
    }

    @Override
    public String login(Email email, Password password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoDataException("회원이 존재하지 않습니다"));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 달라요");
        }
        if (email.equals("admin@example.com")) {
            return jwtTokenProvider.createMyToken(user, List.of("ROLE_ADMIN"));
        }
        List<String> roles = List.of("ROLE_USER");
        return jwtTokenProvider.createMyToken(user, roles);
    }

    @Override
    public void edit(Email email, Password password) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoDataException("해당하는 유저가 없습니다!"));
        userRepository.update(password, email);
    }

    @Override
    public void delete(@AuthenticationPrincipal Email email) {
        userRepository.delete(email);
    }
}