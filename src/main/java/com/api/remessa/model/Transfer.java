package com.api.remessa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(precision = 19, scale = 2)
    private BigDecimal amountBrl;

    @Column(precision = 19, scale = 2)
    private BigDecimal amountUsd;

    @Column(precision = 19, scale = 2)
    private BigDecimal exchangeRate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
