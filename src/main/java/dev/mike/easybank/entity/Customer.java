package dev.mike.easybank.entity;

import dev.mike.easybank.entity.enums.AuthorityType;
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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "customer_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type")
    @Builder.Default
    private Set<AuthorityType> authorities = new HashSet<>(Set.of(AuthorityType.USER));

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    @Override
    @Nonnull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
                .stream()
                .map(authorityType -> new SimpleGrantedAuthority(authorityType.name()))
                .toList();
    }

    // Convenience methods
    public void addAccount(Account account) {
        accounts.add(account);
        account.setOwner(this);
    }
}
