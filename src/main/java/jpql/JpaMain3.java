package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

// JOIN
public class JpaMain3 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
                member.setUsername("member1");
                member.setAge(10);
                member.changeTeam(team);
                em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            // 조인 대상 필터링
            String query2 = "select m from Member m left join m.team t on t.name='teamA'";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
            

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
