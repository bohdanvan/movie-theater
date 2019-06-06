package com.bvan.movietheater;

import static com.bvan.movietheater.entity.Genre.ACTION;
import static com.bvan.movietheater.entity.Genre.ADVENTURE;
import static com.bvan.movietheater.entity.Genre.COMEDY;
import static com.bvan.movietheater.entity.Genre.DRAMA;
import static com.bvan.movietheater.entity.Genre.FANTACY;

import com.bvan.movietheater.entity.Hall;
import com.bvan.movietheater.entity.Movie;
import com.bvan.movietheater.entity.Session;
import com.bvan.movietheater.repository.SessionRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
public class MovieTheaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTheaterApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(EntityManager entityManager, TransactionTemplate transactionTemplate, TestService testService) {
        return args -> {
            transactionTemplate.execute(status -> {
                Movie reference = entityManager.getReference(Movie.class, 1L);
                System.out.println(reference.getInfo());
                return null;
            });
        };
    }
}

@Service
@AllArgsConstructor
class TestService {

    private EntityManager entityManager;
    private TransactionTemplate transactionTemplate;
    private SessionRepository sessionRepository;

    @Transactional
    public void referenceWithNonExistingId() {
        Movie reference = entityManager.getReference(Movie.class, 40L); // proxy, non existing id
        System.out.println(reference.getId());
        System.out.println(reference.getTitle()); // EntityNotFoundException
    }

    public void mergeForDetachedEntity() {
        Movie movie = new Movie()
                .setTitle("Once Upon a Time ... in Hollywood")
                .setReleaseDate(LocalDate.of(2019, 7, 26))
                .setGenres(Arrays.asList(COMEDY, DRAMA)); // Transient

        transactionTemplate.execute(status -> {
            entityManager.persist(movie); // Persistent
            return null;
        });

        System.out.println(movie.getId()); // Detached

        movie.setReleaseDate(LocalDate.of(2019, 7, 20));

        Movie mergedMovie = transactionTemplate.execute(status -> {
//                org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
//                session.saveOrUpdate(movie);

            Movie merged = entityManager.merge(movie);
            return merged; // Persistent
        }); // Detached

        System.out.println(mergedMovie.getReleaseDate()); // 2019-7-20
    }

    @Transactional
    public void delete(long id) {
        Movie movie = entityManager.getReference(Movie.class, id); // ?
        entityManager.remove(movie);
    }

    @Transactional
    public void firstLevelCache() {
        Movie movieA = entityManager.find(Movie.class, 1L);
        Movie movieB = entityManager.find(Movie.class, 1L);

        System.out.println(movieA == movieB); // true
        System.out.println(movieA.equals(movieB)); // true
        System.out.println(movieA.getId().equals(movieB.getId())); // true
    }

    public void firstLevelCacheWithoutTransaction() {
        Movie movieA = entityManager.find(Movie.class, 1L);
        Movie movieB = entityManager.find(Movie.class, 1L); // different entity manager

        System.out.println(movieA == movieB); // false
        System.out.println(movieA.equals(movieB)); // false
        System.out.println(movieA.getId().equals(movieB.getId())); // true
    }

    public void persistentContext() {
        transactionTemplate.execute(status -> {
            List<Movie> movies = entityManager
                    .createQuery("select distinct m from Movie m join fetch m.genres", Movie.class)
                    .getResultList();
            System.out.println(movies.size());

            Movie movie = entityManager.find(Movie.class, -45L);
            System.out.println(movie.getTitle());
            return null;
        });

        transactionTemplate.execute(status -> {
            Movie movie = entityManager.find(Movie.class, -45L);
            System.out.println(movie.getTitle());
            return null;
        });
    }

    @Transactional
    public void flushBeforeSelect() {
        Movie movie = entityManager.find(Movie.class, -45L);
        movie.setTitle("Godzilla: King of the Monsters");

        List<Movie> movies = entityManager
                .createQuery("select distinct m from Movie m join fetch m.genres", Movie.class)
                .getResultList();
        System.out.println(movies.size());
    }

    @Transactional
    private void testDirtyCheck() {
        Movie movie = entityManager.find(Movie.class, -45L);
        movie.setTitle("Godzilla");
    }

    private void persistMovie() {
        Movie movie = new Movie(
                "Godzilla: King of the Monsters",
                LocalDate.of(2019, 5, 31),
                Arrays.asList(ACTION, ADVENTURE, FANTACY)
        ); // transient

        Movie persistedMovie = transactionTemplate.execute(status -> {
            entityManager.persist(movie); // Persistent
            return movie;
        });
        System.out.println(persistedMovie.getId()); // Detached
    }

    private void solutionForNPlusOneProblem() {
        List<Session> sessions = entityManager
                .createQuery("select s from Session s join fetch s.movie join fetch s.hall", Session.class)
                .getResultList();
        for (Session session : sessions) { // 100
            System.out.println("session: " + session.getId() + ", " // 0
                    + "movie: " + session.getMovie().getTitle()); // + 1
        }
    }

    private void nPlusOneProblemWithEntityManager() {
        List<Session> sessions = entityManager
                .createQuery("select s from Session s", Session.class)
                .getResultList();
        for (Session session : sessions) { // 100
            System.out.println("session: " + session.getId() + ", " // 0
                    + "movie: " + session.getMovie().getTitle()); // + 1
        }
    }

    private void nPlusOneProblem() {
        // 100 sessions -> 101 queries
        Iterable<Session> sessions = sessionRepository.findAllById(Arrays.asList(1L, 2L, 3L)); // + 1
        for (Session session : sessions) { // 100
            System.out.println("session: " + session.getId() + ", " // 0
                    + "movie: " + session.getMovie().getTitle()); // + 1
        }
    }

    private void assessToLazyDependency() {
        Session session = sessionRepository.findById(1L).orElseThrow(RuntimeException::new);
        Movie movie = session.getMovie();
        System.out.println(movie.getTitle());
        System.out.println(session);
    }

    private void printAllSessions() {
        Iterable<Session> sessions = sessionRepository.findAll();
        sessions.forEach(System.out::println);
    }
}
