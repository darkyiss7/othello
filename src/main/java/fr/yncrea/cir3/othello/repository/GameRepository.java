package fr.yncrea.cir3.othello.repository;

import fr.yncrea.cir3.othello.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
