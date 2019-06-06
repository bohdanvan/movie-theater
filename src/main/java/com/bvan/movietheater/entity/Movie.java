package com.bvan.movietheater.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "movie_seq")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDate releaseDate;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;

    public Movie(String title, LocalDate releaseDate, List<Genre> genres) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public String getInfo() {
        return "Movie '" + title + "' will be shown on " + releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        Movie movie = (Movie) o;
        return Objects.equals(getTitle(), movie.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
