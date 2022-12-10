package learn.capstone.controllers;

import learn.capstone.domain.GenreService;
import learn.capstone.domain.Result;
import learn.capstone.domain.ResultType;
import learn.capstone.models.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService genreService) {
        this.service = genreService;
    }
    @GetMapping
    public List<Genre> findAll() {
        return service.findAll();
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<Genre> findById(@PathVariable int genreId) {
        Genre genre = service.findById(genreId);
        if (genre == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(genre);
    }

    @PostMapping()
    public ResponseEntity<Object> add(@RequestBody Genre genre){
        Result<Genre> result = service.add(genre);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<Object> update(@PathVariable int genreId, @RequestBody Genre genre) {
        if (genreId != genre.getGenreId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Genre> result = service.update(genre);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> delete(@PathVariable int genreId){
        Result result = service.deleteById(genreId);
        if(result.getType() == ResultType.NOT_FOUND){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }


}
