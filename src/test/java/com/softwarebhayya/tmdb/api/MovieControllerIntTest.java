package com.softwarebhayya.tmdb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarebhayya.tmdb.model.Movie;
import com.softwarebhayya.tmdb.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    void cleanUp(){
        movieRepository.deleteAllInBatch();
    }
    @Test
    void givenMovie_whenCreateMovie_thenReturnSaveMovie() throws Exception {
        // given
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ss rajamouali");
        movie.setActors(List.of("NTR", "Ramcharan", "aliabhatta"));

        // when create movie
        var response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then - verify the response
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value(is(movie.getName())))
                .andExpect(jsonPath("$.director").value(is(movie.getDirector())))
                .andExpect(jsonPath("$.actors").value(is(movie.getActors())));
    }

    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ss rajamouali");
        movie.setActors(List.of("NTR", "Ramcharan", "aliabhatta"));

       Movie savedMovie = movieRepository.save(movie);

       //when
        var response = mockMvc.perform(get("/movies/" + savedMovie.getId()));

        // then - verify the response
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", is(savedMovie.getName())))
                .andExpect(jsonPath("$.director", is(savedMovie.getDirector())))
                .andExpect(jsonPath("$.actors", is(savedMovie.getActors())));
    }

    @Test
    void givenSavedMovie_whenUpdateMovie_thenReturnUpdatedMovie() throws Exception {
        // given - setup data
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("NTR", "Ram Charan", "Alia Bhatt"));

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        // Update movie
        movie.setActors(List.of("NTR", "Ram Charan", "Alia Bhatt", "Ajay Devgan"));

        // when - perform the PUT request to update the movie
        var response = mockMvc.perform(put("/movies/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then - verify the response
        response.andDo(print())
                .andExpect(status().isOk());

        // when - perform the GET request to verify update
        var fetchResponse = mockMvc.perform(get("/movies/" + id));

        // then - verify the updated details
        fetchResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", is(savedMovie.getName())))
                .andExpect(jsonPath("$.director", is(savedMovie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));
    }

    @Test
    void givenSavedMovie_whenDeleteMovie_thenRemoveFromDb() throws Exception {
        // given - setup data
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("NTR", "Ram Charan", "Alia Bhatt"));

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        // when - perform the DELETE request
        mockMvc.perform(delete("/movies/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        // then - verify the movie is removed from the repository
        assertFalse(movieRepository.findById(id).isPresent());
    }

}