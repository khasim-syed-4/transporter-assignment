
package com.assignment.transporter.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "transporter_quotes",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"lane_id", "transporter_id"}
        )
    }
)
public class TransporterQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lane_id", nullable = false)
    private Lane lane;

    @ManyToOne
    @JoinColumn(name = "transporter_id", nullable = false)
    private Transporter transporter;

    @Column(name = "quoted_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal quote;

    public TransporterQuote() {
    }

    public TransporterQuote(Lane lane, Transporter transporter, BigDecimal quote) {
        this.lane = lane;
        this.transporter = transporter;
        this.quote = quote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public BigDecimal getQuote() {
        return quote;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }
}
