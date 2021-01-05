package fr.yncrea.cir3.othello.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "\"utilisateur\"")
public class User implements UserDetails {
    @Id @Column
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Authority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
