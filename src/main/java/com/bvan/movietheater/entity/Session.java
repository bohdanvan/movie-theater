package com.bvan.movietheater.entity;


import static javax.persistence.GenerationType.SEQUENCE;

import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movie_session")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "movie_session_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column
    private LocalDateTime startTime;

    @Column
    private BigDecimal price;

    public Session(Movie movie, Hall hall, LocalDateTime startTime, BigDecimal price) {
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.price = price;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("movie", movie.getId())
                .add("hall", hall.getId())
                .add("startTime", startTime)
                .add("price", price)
                .toString();
    }
}
