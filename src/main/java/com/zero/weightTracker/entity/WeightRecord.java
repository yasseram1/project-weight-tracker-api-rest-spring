package com.zero.weightTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_records")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_record_id")
    private Long weightRecordId;

    @Column(name = "weight_value")
    private Double weight;

    @Column(name = "record_date")
    private LocalDateTime recordDate;

    @ManyToOne
    @JoinColumn(name = "goals_id", nullable = false)
    @JsonIgnore
    private Goal goal;

}
