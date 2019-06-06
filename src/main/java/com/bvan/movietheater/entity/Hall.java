package com.bvan.movietheater.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import com.google.common.base.MoreObjects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Hall {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "hall_seq")
    private Long id;

    @Column
    private String name;

    @Column(name = "rows_count")
    private int rows;

    @Column(name = "seats_count")
    private int seats;

    public Hall(String name, int rows, int seats) {
        this.name = name;
        this.rows = rows;
        this.seats = seats;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("rows", rows)
                .add("seats", seats)
                .toString();
    }
}
