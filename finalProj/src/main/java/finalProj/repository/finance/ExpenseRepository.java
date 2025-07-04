package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

}
