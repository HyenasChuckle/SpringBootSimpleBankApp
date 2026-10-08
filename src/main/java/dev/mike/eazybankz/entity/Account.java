package dev.mike.eazybankz.entity;

import dev.mike.eazybankz.entity.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String number;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;
}
