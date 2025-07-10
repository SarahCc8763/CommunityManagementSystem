package finalProj.repository.ticket;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import finalProj.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	@Query(value = """
			    SELECT t.*
			    FROM ticket t
			    LEFT JOIN issue_type_and_ticket itat ON t.id = itat.ticket_id
			    LEFT JOIN issue_type it ON it.id = itat.issue_type_id
			    WHERE
			        (:title IS NULL OR t.title LIKE %:title%)
			        AND (:status IS NULL OR t.status = :status)
			        AND (:startDate IS NULL OR CAST(t.start_date AS DATE) >= CAST(:startDate AS DATE))
			        AND (:reporterId IS NULL OR t.reporter_id = :reporterId)
			        AND (:issueTypeSize = 0 OR it.issue_type_name IN (:issueTypes))
			    GROUP BY t.id, t.reporter_id, t.assigner_id, t.title, t.status, t.issue_description,
			             t.cost, t.action_time, t.action_by, t.start_date, t.end_date, t.notes, t.community_id
			    HAVING (:issueTypeSize = 0 OR COUNT(DISTINCT it.issue_type_name) = :issueTypeSize)
			""", nativeQuery = true)
	List<Ticket> searchTickets(
			@Param("title") String title,
			@Param("status") String status,
			@Param("startDate") LocalDate startDate,
			@Param("reporterId") Integer reporterId,
			@Param("issueTypes") List<String> issueTypes,
			@Param("issueTypeSize") int issueTypeSize);

	@NonNull
	@EntityGraph(attributePaths = { "reporter" })
	List<Ticket> findAll();

}
