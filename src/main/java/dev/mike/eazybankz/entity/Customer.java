package dev.mike.eazybankz.entity;

import dev.mike.eazybankz.entity.enums.AuthorityType;
import dev.mike.eazybankz.entity.enums.Status;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "customer_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type")
    @Builder.Default
    private Set<AuthorityType> authorities = new HashSet<>(Set.of(AuthorityType.USER));

    @Override
    @Nonnull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(type -> new SimpleGrantedAuthority(type.name()))
                .toList();
    }

    @Override
    @Nonnull
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(Status.ACTIVE);
    }
}
