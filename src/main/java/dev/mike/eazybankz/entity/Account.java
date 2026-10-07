package dev.mike.eazybankz.entity;

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

    @Column(name ="iban", unique = true, nullable = false)
    private String IBAN;

    @Column(name ="current_balance", nullable = false)
    private BigDecimal currentBalance;

    @ManyToOne
    @JoinColumn(name ="account_owner_id", nullable = false)
    private Customer accountOwner;
}
