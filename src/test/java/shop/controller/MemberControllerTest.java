package shop.controller;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "application-test.yml")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    // 어드민 사용자 생성 메서드
    private Member createAdmin(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("주인");
        memberFormDto.setAddress("인천시 부평구");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        member.setRole(Role.ADMIN); // 수정된 부분    롤이 열거형이라서 열거형으로
        return memberService.saveMember(member); // 사용자 저장
    }

    // 일반 사용자 생성 메서드
    private Member createUser(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("사용자");
        memberFormDto.setAddress("서울시 마포구");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        member.setRole(Role.USER); // 일반 사용자 역할 설정
        return memberService.saveMember(member); // 사용자 저장
    }

    @Test
    @DisplayName("주인장 로그인 성공 테스트")
    public void adminLoginSuccessTest() throws Exception {
        String email = "admin@email.com";
        String password = "admin1234";

        // 어드민 사용자 생성
        createAdmin(email, password);

        // 로그인 테스트
        mockMvc.perform(formLogin()
                        .userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("일반 사용자 로그인 성공 테스트")
    public void userLoginSuccessTest() throws Exception {
        String email = "user@email.com";
        String password = "user1234";

        // 일반 사용자 생성
        createUser(email, password);

        // 로그인 테스트
        mockMvc.perform(formLogin()
                        .userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }
}
