package learn.capstone.domain;

import learn.capstone.data.GenreRepository;
import learn.capstone.data.MediaRepository;
import learn.capstone.data.ReviewRepository;
import learn.capstone.models.Genre;
import learn.capstone.models.Media;
import learn.capstone.models.Review;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private final MediaRepository repository;
    private final ReviewRepository reviewRepository;
    private final GenreRepository genreRepository;
    public MediaService(MediaRepository repository, ReviewRepository reviewRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
        this.genreRepository = genreRepository;
    }

    public List<Media> findAll(){
        List<Media> mediaList = repository.findAll();
        for(Media m: mediaList){
            if(m.getReviews() != null){
                m.setMean_stars(findAverageRating(m));

                Comparator<Media> compareByRatingThenId =
                        Comparator.comparing(Media::getMean_stars)
                                .thenComparing(Media::getMediaId);

                List<Media> sortedMedia = mediaList.stream()
                        .sorted(compareByRatingThenId)
                        .collect(Collectors.toList());

                Collections.reverse(sortedMedia);

                return sortedMedia;
            } else {
                Comparator<Media> compareByRatingThenId =
                        Comparator.comparing(Media::getMean_stars)
                                .thenComparing(Media::getMediaId);

                List<Media> sortedMedia = mediaList.stream()
                        .sorted(compareByRatingThenId)
                        .collect(Collectors.toList());

                Collections.reverse(sortedMedia);

                return sortedMedia;
            }
        }
        return repository.findAll();
    }//findAll

    public Media findById(int mediaId) {
        List<Media> mediaList = repository.findAll();
        for(Media m: mediaList){
            if(m.getReviews() != null && m.getMediaId()==mediaId){
                m.setMean_stars(findAverageRating(m));
                return m;
            } else {
                return repository.findById(mediaId).orElse(null);
            }
        }
        return repository.findById(mediaId).orElse(null);
    }//findById

    public List<Media> findByGenre(List<Genre> genreList){
        List<Media> container = new ArrayList<>();
        List<Genre> genreListTwo = genreRepository.findByGenreIdIn(genreList.stream().map(g -> g.getGenreId()).toList());
        List<Media> all = genreListTwo.stream().flatMap(g -> g.getMedia().stream()).distinct().toList();
        for(Media m: all){
            if(m.getGenres().containsAll(genreList)){
               container.add(m);
            }
        }
        return container;
    }//findByGenre

//    public List<Review> findReviewsByMedia (Media media){
//        List<Review> reviewList = reviewRepository.findAll();
//        List<Review> matchingReviews = new ArrayList<>();
//        for(Review r: reviewList){
//            if(r.getMedia().getMediaId()==media.getMediaId() && r.getMedia() != null){
//                matchingReviews.add(r);
//            }
//        }
//        return matchingReviews;
//    }//findReviewsByMedia

    public int findMediaIdByMediaTitle (Media media){
        int mediaId = media.getMediaId();
        return mediaId;
    }//findMediaIdByMediaTitle

    public Result<Media> add(Media media) {
        Result<Media> result = validate(media);
        List<Media> mediaList = findAll();
        if(!result.isSuccess()){
            return result;
        }

        if (media != null && media.getMediaId() != 0) {
            result.addMessage("Media Id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (media != null) {
            for(Media m : mediaList){
                if(media.getTitle().equalsIgnoreCase(m.getTitle()) && media.getType().equalsIgnoreCase(m.getType()) && media.getYear()==m.getYear()){
                    result.addMessage("This matches something already in the system.", ResultType.INVALID);
                    return result;
                }
            }
        }

        Media thingToAdd = media;
//        thingToAdd.setMediaId(media.getMediaId());
        thingToAdd.setTitle(media.getTitle());
        thingToAdd.setType(media.getType());
        thingToAdd.setMean_stars(media.getMean_stars());
        thingToAdd.setYear(media.getYear());
//        thingToAdd.setMean_stars(findAverageRating(media));//just added
        thingToAdd.setLink(media.getLink());
        assert thingToAdd != null;
        media = repository.save(thingToAdd);
        result.setPayload(media);
        return result;
    }//add

    public Result<Media> update(Media media) {
        Result<Media> result = validate(media);
        if(!result.isSuccess()){
            return result;
        }

        if (media.getMediaId() <= 0) {
            result.addMessage("mediaId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (result.isSuccess()) {
            Media thingToUpdate = repository.findById(media.getMediaId()).orElse(null);

            assert thingToUpdate != null;
            thingToUpdate.setTitle(media.getTitle());
            thingToUpdate.setYear(media.getYear());
            thingToUpdate.setType(media.getType());
            thingToUpdate.setLink(media.getLink());
            thingToUpdate.setMean_stars(media.getMean_stars());

            media = repository.save(thingToUpdate);
            result.setPayload(media);

            } else {
                result.addMessage("Media ID %s was not found.", ResultType.NOT_FOUND, media.getMediaId());
            }
        return result;
    }//update

    public Result<Media> deleteById(int mediaId) {
        Result<Media> result = new Result();
        if (!repository.existsById(mediaId)) {
            String msg = String.format("Media Id: %s is not found.", mediaId);
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

        if (result.isSuccess()) {
            repository.deleteById(mediaId);
        }

        return result;
    }//deleteById

    //Helper Methods

    public double findAverageRating(Media media){
        List<Review> reviews = reviewRepository.findAll();
        double countOfStars = 0;
        double countOfReviews = 0;
        double average = 0;
        for(Review r: reviews){
            if(r.getMedia().getMediaId() == media.getMediaId()){
                countOfStars += r.getStars();
                countOfReviews ++;
                average = countOfStars / countOfReviews;
            }
        }
        return average;
    }//findAverageRating

    private Result validate(Media media) {
        Result<Media> result = new Result();
        if (media == null) {
            result.addMessage("Media cannot be null", ResultType.INVALID);
            return result;
        }
        if (Validations.isNullOrBlank(media.getTitle())) {
            result.addMessage("Title is required", ResultType.INVALID);
        }

        if(result.isSuccess()){
            List<Media> existingMedias = repository.findAll();

                for (Media existingMedia : existingMedias) {
                    if (existingMedia.getMediaId() != media.getMediaId() &&
                            existingMedia.getTitle().equalsIgnoreCase(media.getTitle()) &&
                            existingMedia.getYear() == media.getYear() &&
                            existingMedia.getType() == (media.getType())) {
                        result.addMessage("The title, year, and type are all the same, but the ID does not match. This is likely a problem.",
                                ResultType.INVALID);
                                return result;
                }
            }
        }
        return result;
    }//validate


}//end
