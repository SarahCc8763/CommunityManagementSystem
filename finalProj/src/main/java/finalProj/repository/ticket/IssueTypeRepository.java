package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domin.ticket.IssueType;


public interface IssueTypeRepository extends JpaRepository<IssueType, Integer> {
    
}
