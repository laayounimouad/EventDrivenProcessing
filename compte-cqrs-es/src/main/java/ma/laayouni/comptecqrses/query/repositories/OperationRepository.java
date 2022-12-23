package ma.laayouni.comptecqrses.query.repositories;

import ma.laayouni.comptecqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, String> {
}
