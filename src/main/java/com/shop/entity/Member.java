package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.repository.MemberRepository;
import com.shop.service.MailService;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    // Role 값을 String 값으로 저장
    @Enumerated(EnumType.STRING)
    private Role role;

    // 소셜 로그인 여부
    private boolean social;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        member.setSocial(false);    // false - 일반 회원 / true - social 로그인 회원

        return member;
    }

    public void updatePassword(String encodedPassword) {
        try {
            setPassword(encodedPassword); // 새로운 암호로 업데이트
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace(); // 혹은 로깅
        }
    }


}
