package com.example.searchvirtual.controller;


import com.example.searchvirtual.domain.User;
import com.example.searchvirtual.repository.UserRepository;
import com.example.searchvirtual.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (userRepository.findByUsername(username).isPresent()) {
            throw  new RuntimeException("이미 존재하는 사용자입니다.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        Map<String,Object> result = new HashMap<>();
        result.put("message","회원 가입 성공");
        return result;

    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("사용자를 찾을수없습니다."));

        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(),user.getRole());

        Map<String,Object> result = new HashMap<>();
        result.put("token",token);
        result.put("message","로그인성공");
        return result;
    }
}
