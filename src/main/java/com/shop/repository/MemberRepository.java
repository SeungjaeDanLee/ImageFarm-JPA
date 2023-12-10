package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.password = :password WHERE m.email = :email")
    void updatePassword(String password, String email);

    Page<Member> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Member> findByEmailContainingIgnoreCase(String keyword, Pageable pageable);

//    void deleteMember(Long memberId);

//    Page<Member> getMemberPage(Pageable pageable);

}