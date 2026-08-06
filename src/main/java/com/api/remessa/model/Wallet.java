package com.api.remessa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User userId;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceBrl;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceUsd;
}
