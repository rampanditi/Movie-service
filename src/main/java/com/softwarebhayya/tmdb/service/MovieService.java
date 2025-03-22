package com.softwarebhayya.tmdb.service;

import com.softwarebhayya.tmdb.exception.*;
import com.softwarebhayya.tmdb.model.*;
import com.softwarebhayya.tmdb.repository.*;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public Movie create(Movie movie) {
        if (movie == null) {
            throw new InvalidDataException("Invalid Movie: null");
        }
        return movieRepository.save(movie);
    }

    public Movie read(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
    }

    public void update(Long id, Movie update) {
        if (update == null || id == null) {
            throw new InvalidDataException("Invalid Movie or ID: null");
        }
        if (movieRepository.existsById(id)) {
            Movie movie = movieRepository.getReferenceById(id);
            movie.setName(update.getName());
            movie.setDirector(update.getDirector());
            movie.setActors(update.getActors());
            movieRepository.save(movie);
        } else {
            throw new NotFoundException("Movie not found with id: " + id);
        }
    }

    public void delete(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new NotFoundException("Movie not found with id: " + id);
        }
    }
}