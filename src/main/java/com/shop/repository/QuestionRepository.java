package com.shop.repository;

import java.util.List;


import com.shop.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    @Query("select distinct q from Question q "
            + "left join q.member u1 "  // q.author를 q.member로 수정
            + "left join Answer a on a.question=q "
            + "left join a.member u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.name like %:kw% "  // username 대신 name 사용
            + "   or a.content like %:kw% "
            + "   or u2.name like %:kw% ") // username 대신 name 사용
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);


}
