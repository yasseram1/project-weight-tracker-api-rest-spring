package com.zero.weightTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "goals")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goals_id")
    private Long goalsId;

    @NotNull
    @Column(name = "start_weight")
    private Double startWeight;

    @NotNull
    @Column(name = "goal_weight")
    private Double goalWeight;

    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WeightRecord> weightRecords;

}
