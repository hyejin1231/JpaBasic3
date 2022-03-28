package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

// JOIN
public class JpaMain4 {

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

            String query = "select function('group_concat', m.username) From Member m";

            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
