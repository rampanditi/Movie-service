package com.softwarebhayya.tmdb.api;

import com.softwarebhayya.tmdb.exception.*;
import com.softwarebhayya.tmdb.model.*;
import com.softwarebhayya.tmdb.service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

//    @GetMapping("/{id}")
//    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
//        Movie movie = movieService.read(id); // Throws NotFoundException if not found
//        log.info("Returned movie with id: {}", id);
//        return ResponseEntity.ok(movie);
//    }
@GetMapping("/{id}")
public ResponseEntity<Movie> getMovie(@PathVariable(required = false) Long id) {
    if (id == null) {
        throw new InvalidDataException("Path variable 'id' is required");
    }
    Movie movie = movieService.read(id);
    log.info("Returned movie with id: {}", id);
    return ResponseEntity.ok(movie);
}

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody(required = false) Movie movie) {
        if (movie == null) {
            throw new InvalidDataException("Request body is required");
        }
        Movie createdMovie = movieService.create(movie);
        log.info("Created movie with id: {}", createdMovie.getId());
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long id, @RequestBody (required = false) Movie movie) {
        log.info("Updating movie with id: {}", id);
        if (movie == null) {
            throw new InvalidDataException("Request body is required");
        }
        movieService.update(id, movie);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("Deleting movie with id: {}", id);
        movieService.delete(id);
        return ResponseEntity.ok().build();
    }
}