package learn.capstone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mediaId;
    private String title;
    private int year;
    private String type;
    private String link;
    private String imageUrl;
    private double mean_stars;

    @ManyToMany
//    @ManyToMany(cascade = CascadeType.ALL)//added cascade initially, but had to turn this off
    @JsonManagedReference
    @JoinTable(name="media_genre", joinColumns = @JoinColumn(name="media_id"), inverseJoinColumns = @JoinColumn(name="genre_id"))
    @JsonIgnore
    private List<Genre> genres;

    @OneToMany
    @JoinTable(name="review", joinColumns = @JoinColumn(name="media_id"))
    @JsonIgnore
    private List<Review> reviews;

    public Media(){};

    public Media(int mediaId, String title, int year, String type, String link, String imageUrl, double mean_stars) {
        this.mediaId = mediaId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.link = link;
        this.imageUrl = imageUrl;
        this.mean_stars = mean_stars;
    }//Media constructor

    public Media(int mediaId, String title, int year, String type, String link, double mean_stars) {
        this.mediaId = mediaId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.link = link;
        this.mean_stars = mean_stars;
    }//Media constructor

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl(){return imageUrl;}

    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}

    public double getMean_stars() {
        return mean_stars;
    }

    public void setMean_stars(double mean_stars) {
        this.mean_stars = mean_stars;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}//end
