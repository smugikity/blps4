package com.football.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="team1_id", nullable=false)
    private Team team1;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="team2_id", nullable=false)
    private Team team2;
    @Nullable
    private byte team1_score=0;
    @Nullable
    private byte team2_score=0;

}