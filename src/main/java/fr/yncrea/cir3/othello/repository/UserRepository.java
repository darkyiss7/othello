package fr.yncrea.cir3.othello.repository;

import fr.yncrea.cir3.othello.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
