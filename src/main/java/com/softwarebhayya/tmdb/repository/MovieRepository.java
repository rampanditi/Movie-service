package com.softwarebhayya.tmdb.repository;

import com.softwarebhayya.tmdb.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
