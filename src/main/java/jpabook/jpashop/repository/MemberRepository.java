package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
// @Repository 어노테이션을 사용하면 ComponentSacn에 의해서 자동으로 스프링 bean으로 관리 된다.
@Repository // @Repository Ctrl 클릭해서 보면 @Component 어노테이션이 있는데 그 어노테이션이 있으면 컴포넌트 스캔의 대상이된다.
public class MemberRepository {

    @PersistenceContext
    private EntityManager em; // 스프링이 EntityManager를 만들어서 걔를 주입해준다.

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // 첫번째가 JPQL, 두번째가 반환타입
        /*List<Member> result = em.createQuery("select  m from Member m", Member.class)
                .getResultList();
        return result;*/
        
        // inline으로 변경함 , 조회된 결과를 반환함
                                // 첫번째가 JPQL, 두번째가 반환타입
        // 참고로 sql은 테이블 대상으로 쿼리를 하는데, JPQL은 엔티티 객체를 대상으로 쿼리를한다.
        // 즉 엔티티 멤버 m을 조회해라
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 이름으로 Member 검색하기
    public List<Member> findByName(String name){
        // 파라미터 바인딩해서 특정 회원의 이름만 찾는다.
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
