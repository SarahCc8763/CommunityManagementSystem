package finalProj.repository.ticket;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.IssueType;


public interface IssueTypeRepository extends JpaRepository<IssueType, Integer> {
	Optional<IssueType> findByIssueTypeName(String issueTypeName);
}
