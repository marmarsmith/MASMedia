package learn.capstone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    private int stars;
    private String opinion;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name="app_user_id",
            foreignKey=@ForeignKey(name="fk_app_user_review_id"))
    private AppUser user;
    @ManyToOne
    @JoinColumn(name="media_id",
            foreignKey=@ForeignKey(name="fk_media_review_id"))
    private Media media;

    public Review(int reviewId, int stars, String opinion, AppUser user, Media media) {
        this.reviewId = reviewId;
        this.stars = stars;
        this.opinion = opinion;
        this.user = user;
        this.media = media;
    }

    public Review() {
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}