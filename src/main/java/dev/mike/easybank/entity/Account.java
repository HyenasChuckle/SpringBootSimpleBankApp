package dev.mike.easybank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "senderAccount", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setSenderAccount(this);
    }
}
