package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	@Query(value = """
		    SELECT t.*
		    FROM ticket t
		    LEFT JOIN issue_type_and_ticket itat ON t.id = itat.ticket_id
		    LEFT JOIN issue_type it ON it.id = itat.issue_type_id
		    WHERE
		        (:id IS NULL OR t.id = :id)
		        AND (:reporterId IS NULL OR t.reporter_id = :reporterId)
		        AND (:assignerId IS NULL OR t.assigner_id = :assignerId)
		        AND (:title IS NULL OR t.title LIKE %:title%)
		        AND (:size = 0 OR it.issue_type_name IN (:keywords))
		    GROUP BY t.id, t.reporter_id, t.assigner_id, t.title, t.status, t.issue_description,
		             t.cost, t.action_time, t.action_by, t.start_date, t.end_date, t.notes, t.community_id
		    HAVING (:size = 0 OR COUNT(DISTINCT it.issue_type_name) = :size)
		""", nativeQuery = true)
		List<Ticket> searchTicketsWithOptionalIssueTypes(
		    @Param("id") Integer id,
		    @Param("reporterId") Integer reporterId,
		    @Param("assignerId") Integer assignerId,
		    @Param("title") String title,
		    @Param("keywords") List<String> keywords,
		    @Param("size") int size
		);
}
