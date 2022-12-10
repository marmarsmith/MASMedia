package learn.capstone.domain;

import learn.capstone.data.ReviewRepository;
import learn.capstone.models.AppUser;
import learn.capstone.models.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public Review findById(int reviewId) {
        return repository.findById(reviewId).orElse(null);
    }

    public List<Review> findByUsername(String username) {
        List<Review> all = repository.findAll().stream()
                .filter(r -> r.getUser().getUsername().equalsIgnoreCase(username)).toList();

        all.forEach(r -> { r.getUser().setPassword("");});

        return all;
    }

    public List<Review> findByMediaId(int mediaId) {
        List<Review> all = repository.findAll().stream()
                .filter(r -> r.getMedia().getMediaId() == mediaId).toList();

        all.forEach(r -> { r.getUser().setPassword("");});

        return all;
    }

    public Result<Review> add(Review review) {
        Result<Review> result = validate(review);

        if (review != null && review.getReviewId() != 0) {
            result.addMessage("Review `review_id` should not be set for `add`", ResultType.INVALID);
        }

        if (result.isSuccess()) {
            repository.save(review);

            review.getUser().setPassword("");
            result.setPayload(review);
        }

        return result;
    }

    public Result<Review> update(Review review) {
        Result<Review> result = validate(review);

        if (!result.isSuccess()) {
            return result;
        }

        if (review.getReviewId() <= 0) {
            result.addMessage("Review `review_id` should be set.", ResultType.INVALID);
            return result;
        }

        Review old = repository.findById(review.getReviewId()).orElse(null);

        if (old != null) {
            if (review.getUser().getAppUserId() != old.getUser().getAppUserId()) {
                result.addMessage("Review must have the same user.", ResultType.INVALID);
            }
            if (review.getMedia().getMediaId() != old.getMedia().getMediaId()) {
                result.addMessage("Review must have the same media.", ResultType.INVALID);
            }
        }

        if (result.isSuccess()) {

            if (old != null) {
                old.setStars(review.getStars());
                old.setOpinion(review.getOpinion());
                repository.save(old);

                review.getUser().setPassword("");
                result.setPayload(review);
            } else {
                result.addMessage("Review ID %s was not found.", ResultType.NOT_FOUND, review.getReviewId());
            }
        }

        return result;
    }

    public Result<Review> deleteById(int reviewId) {
        Result<Review> result = new Result<>();

        if (!repository.existsById(reviewId)) {
            result.addMessage("Review ID %s was not found.", ResultType.NOT_FOUND, reviewId);
        } else {
            repository.deleteById(reviewId);
        }
        return result;
    }

    private Result<Review> validate(Review review) {
        Result<Review> result = new Result<>();

        if (review == null) {
            result.addMessage("Review cannot be null.", ResultType.INVALID);
            return result;
        }

        if (review.getUser() == null || review.getUser().getAppUserId() == 0) {
            result.addMessage("Review must have a user.", ResultType.INVALID);
        }

        if (review.getMedia() == null || review.getMedia().getMediaId() == 0) {
            result.addMessage("Review must have media attached.", ResultType.INVALID);
        }

        if (result.isSuccess()) {
            List<Review> all = repository.findAll();

            for (Review other : all) {
                if (other.getReviewId() != review.getReviewId() &&
                        other.getUser().getAppUserId() == review.getUser().getAppUserId() &&
                        other.getMedia().getMediaId() == review.getMedia().getMediaId()
                ) {
                    result.addMessage("Review user and media must be unique.", ResultType.INVALID);
                }
            }
        }
        return result;
    }

}
