package com.bvan.movietheater.entity;

import com.google.common.base.MoreObjects;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name = "row_num")
    private int row;

    @Column(name = "seat_num")
    private int seat;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user.getId())
                .add("session", session.getId())
                .add("row", row)
                .add("seat", seat)
                .add("createdDate", createdDate)
                .add("status", status)
                .toString();
    }
}
