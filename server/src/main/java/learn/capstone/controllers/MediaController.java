package learn.capstone.controllers;

import learn.capstone.domain.MediaService;
import learn.capstone.domain.Result;
import learn.capstone.domain.ResultType;
import learn.capstone.models.Genre;
import learn.capstone.models.Media;
import learn.capstone.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/media")
public class MediaController {
    private final MediaService service;

    public MediaController(MediaService mediaService) {
        this.service = mediaService;
    }
    @GetMapping
    public List<Media> findAll() {
        return service.findAll();
    }//findAll

    @GetMapping("/{mediaId}")
    public ResponseEntity<Media> findById(@PathVariable int mediaId) {
        Media media = service.findById(mediaId);
        if (media == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(media);
    }//findById

    @GetMapping("/search/{genreList}")
    @ResponseBody
    public List<Media> findByGenre(@PathVariable List<Genre> genreList) {
        List<Media> mediaList = service.findByGenre(genreList);
        return mediaList;
    }//findByGenre

//    @GetMapping("/search/review/{media}")
//    @ResponseBody
//    public List<Review> findReviewsByMedia(@PathVariable Media media) {
//        List<Review> reviewList = service.findReviewsByMedia(media);
//        return reviewList;
//    }//findByGenre

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Media media) {
        Result<Media> result = service.add(media);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }//add

    @PutMapping("/{mediaId}")
    public ResponseEntity<Object> update(@PathVariable int mediaId, @RequestBody Media media) {
        if (mediaId != media.getMediaId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Media> result = service.update(media);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }//update

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> delete(@PathVariable int mediaId){
        Result result = service.deleteById(mediaId);
        if(result.getType() == ResultType.NOT_FOUND){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }//delete

}//end
