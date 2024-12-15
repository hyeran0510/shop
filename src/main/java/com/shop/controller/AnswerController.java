package com.shop.controller;

import java.security.Principal;

import com.shop.dto.AnswerDto;
import com.shop.dto.QuestionDto;
import com.shop.entity.Answer;
import com.shop.entity.Member;
import com.shop.entity.Question;
import com.shop.service.AnswerService;
import com.shop.service.MemberService;
import com.shop.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerDto answerDto,
                               BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        Member member = this.memberService.getMember(principal.getName());

        if (bindingResult.hasErrors()) {
            QuestionDto questionDto = new QuestionDto(); // 필요에 따라 question 정보를 이용해 questionDto 생성
            // questionDto에 필요한 데이터 설정
            questionDto.setSubject(question.getSubject());
            questionDto.setContent(question.getContent());
            model.addAttribute("question", questionDto); // questionDto를 모델에 추가
            model.addAttribute("answerForm", answerDto);
            return "question_detail";
        }

        Answer answer = this.answerService.create(question, answerDto.getContent(), member);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(@PathVariable("id") Integer id, Model model, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getMember().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        AnswerDto answerDto = new AnswerDto();
        answerDto.setContent(answer.getContent());
        model.addAttribute("answerForm", answerDto); // answerForm을 Model에 추가
        return "answer_form";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerDto answerDto, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("answerForm", answerDto); // answerForm을 다시 추가
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getMember().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerDto.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getMember().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        Member member = this.memberService.getMember(principal.getName()); // 수정된 부분
        this.answerService.vote(answer, member);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}
