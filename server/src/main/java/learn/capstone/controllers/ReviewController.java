package learn.capstone.controllers;

import learn.capstone.domain.AppUserService;
import learn.capstone.domain.Result;
import learn.capstone.domain.ResultType;
import learn.capstone.domain.ReviewService;
import learn.capstone.models.AppUser;
import learn.capstone.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService service;
    private final AppUserService userService;

    public ReviewController(ReviewService service, AppUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username, @AuthenticationPrincipal AppUser principal) {
        AppUser user = userService.findByUsername(username);
        if (principal.getUsername().equalsIgnoreCase(username)
                || principal.hasAuthority("ADMIN")
                || !user.isPrivateProfile()) {
            List<Review> reviews = service.findByUsername(username);
            return ResponseEntity.ok(reviews);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/profile/{username}/{reviewId}")
    public ResponseEntity<?> getUserReview(@PathVariable int reviewId, @PathVariable String username,
                                    @AuthenticationPrincipal AppUser principal) {
        Review review = service.findById(reviewId);

        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!principal.hasAuthority("ADMIN") && !principal.getUsername().equalsIgnoreCase(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (reviewId != review.getReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        if (review.getUser().getAppUserId() != principal.getAppUserId()) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        return ResponseEntity.ok(review);
    }

    @GetMapping("/profile/{username}/{reviewId}/delete")
    public ResponseEntity<?> getUserReview(@PathVariable String username,@PathVariable int reviewId,
                                    @AuthenticationPrincipal AppUser principal) {
        Review review = service.findById(reviewId);

        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!principal.getUsername().equalsIgnoreCase(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (reviewId != review.getReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        if (review.getUser().getAppUserId() != principal.getAppUserId()) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        return ResponseEntity.ok(review);
    }

    @GetMapping("/media/review/{mediaId}")
    public ResponseEntity<?> findByMediaId(@PathVariable int mediaId) {
        List<Review> reviews = service.findByMediaId(mediaId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/review/id/{reviewId}")
    public ResponseEntity<?> findById(@PathVariable int reviewId, @AuthenticationPrincipal AppUser principal) {
        if (!principal.hasAuthority("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Review review = service.findById(reviewId);
        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(review);
    }

    @PostMapping("/review/add")
    public ResponseEntity<?> add(@RequestBody Review review) {
        Result<Review> result = service.add(review);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/profile/{username}/{reviewId}")
    public ResponseEntity<?> update(@PathVariable int reviewId, @RequestBody Review review,
                                    @PathVariable String username,
                                    @AuthenticationPrincipal AppUser principal) {
        if (!principal.getUsername().equalsIgnoreCase(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (reviewId != review.getReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (review.getUser().getAppUserId() != principal.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<Review> result = service.update(review);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/profile/{username}/{reviewId}/delete")
    public ResponseEntity<?> delete(@PathVariable String username,@PathVariable int reviewId,
                                    @AuthenticationPrincipal AppUser principal) {
        Result<Review> result;

        try {
            if (principal.hasAuthority("ADMIN") ||
                    service.findById(reviewId).getUser().getUsername().equalsIgnoreCase(username)) {
                result = service.deleteById(reviewId);
                if(result.getType() == ResultType.NOT_FOUND) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/review/id/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable int reviewId, @AuthenticationPrincipal AppUser principal) {
        Result<Review> result;

        try {
            if (principal.hasAuthority("ADMIN") || service.findById(reviewId).getUser() == principal) {
            result = service.deleteById(reviewId);
                if(result.getType() == ResultType.NOT_FOUND) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
