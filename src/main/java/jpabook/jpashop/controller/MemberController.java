package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm()); // MemberForm()객체를 "memberForm" 이름으로 넘겨준다
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){ // @Valid 는 validation 기능
        // Member로 안받고 MemberForm으로 받은건 RequestDto 대신 MemberForm으로 썼다고 생각하면됨
        // validate 했을 때 문제가 발생하면 뒤에 BindingResult 가 있으면 오류가 담긴다.
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        /*Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);*/
        Member member = Member.builder()
                        .name(form.getName())
                        .address(address)
                        .build();

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        // responseDto 쓰는게 정석이다 그냥 간단하니까 entity를 사용했다.
        // api를 사용할 때는 절대 entity를 넘기지 마라
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
