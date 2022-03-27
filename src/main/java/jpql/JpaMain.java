package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            // 영속성 컨텍스트 관리 받음
            // 엔티티 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();

            // 엔티티 프로젝션, join 사용 -> 이런 경우에는 "select t from Member m join m.team t" 로 사용해야함
            List<Team> resultTeam = em.createQuery("select m.team from Member m", Team.class).getResultList();

            // 임베디드 타입 프로젝션
            List<Address> resultOrder = em.createQuery("select o.address from Order o", Address.class).getResultList();

            // 스칼라 타입 프로젝션
            List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

            // 스칼라 타입 방법 1. Object[] 타입으로 조회
            Object o = resultList.get(0);
            Object[] result1 = (Object[]) o;
            System.out.println("username = " + result1[0]);
            System.out.println("age = " + result1[1]);


            // 스칼라 타입 방법 2. new 명령어로 조회
            List<MemberDto> resultDto = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class).getResultList();

            MemberDto memberDto = resultDto.get(0);
            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
            System.out.println("memberDto.getAge() = " + memberDto.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
