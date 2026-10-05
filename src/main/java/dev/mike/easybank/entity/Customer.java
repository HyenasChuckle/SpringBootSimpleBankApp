package dev.mike.easybank.entity;

import dev.mike.easybank.entity.enums.AuthorityType;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
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
    private Set<AuthorityType> authorities;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Account> accounts;
}
