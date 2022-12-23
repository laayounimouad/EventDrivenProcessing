package ma.laayouni.comptecqrses.query.repositories;

import ma.laayouni.comptecqrses.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
