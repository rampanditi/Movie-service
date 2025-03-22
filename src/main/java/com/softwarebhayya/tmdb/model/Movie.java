package com.softwarebhayya.tmdb.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String director;
    @ElementCollection
    private List<String> actors = new ArrayList<>();
}
