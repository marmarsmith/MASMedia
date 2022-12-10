
import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, save } from "./services/review-api";
import { findById as findByMediaId } from "./services/media-api";
import { findByUsername } from "./services/user-api";
import AuthContext from "./contexts/AuthContext";
import React from 'react';
import Stars from './components/Stars';

const emptyReview = {
    stars: "",
    opinion: "",
    type: "",
    link: "",
    media: "",
    user: ""
};


  

function ReviewForm() {

    const [stars, setStars] = useState(0);

    const ratingChanged = (newRating) => {
      console.log(newRating)
      setStars(newRating)
    }

    const [review, setReview] = useState(emptyReview);
    const [media, setMedia] = useState(null);
    const [user, setUser] = useState(null);
    const { reviewId, username, mediaId } = useParams();
    const history = useHistory();
    const authContext = useContext(AuthContext);

    const handleErr = useCallback(err => {
        if (err === 403) {
            err = "Unauthorized";
        }
        history.push(`/api/media`, err.toString())
    }, [authContext, history]);

    

    useEffect(() => {
        if (reviewId) {
            findById(username, reviewId)
                .then(result => {
                    setReview(result)
                    setStars(result.stars)
                })
                .catch(handleErr);
        } else if (mediaId) {
            findByMediaId(mediaId)
            .then(result =>  {setMedia(result); console.log(result)})
            .catch(handleErr);

            findByUsername(authContext.credentials.username)
            .then(result => setUser(result))
            .catch(handleErr);
        }

    }, [username, reviewId, mediaId, history, authContext.credentials.username, handleErr]);

    const handleChange = evt => {
        const next = { ...review};
        let value = evt.target.value;
        
        next[evt.target.name] = value;
        setReview(next);
    };

    const onSubmit = evt => {
        evt.preventDefault();
        (review.reviewId ? save({...review, stars}) : save({...review, media, stars, user: {appUserId : user.appUserId}}))
            .then(() => history.push(`/api/profile/${authContext.credentials.username}`))
            .catch(handleErr);
            
    };

    return (
        <form onSubmit={onSubmit}>
            <h2>{review.reviewId ? `Update Review for ${review.media.title}` : `Add Review for ${media?.title}`}</h2>
            
            
            <div id="stars" className="col">
                <label htmlFor="opinion" className="form-label">Rating</label>
                <Stars stars={stars} ratingChanged={ratingChanged} edit={true}/>
            </div>
            
            <div className="mb-2">
                <label htmlFor="opinion" className="form-label">Opinion</label>
                <br></br>
                <textarea id="opinion" name="opinion" rows="4" cols="50" required
                    value={review.opinion} onChange={handleChange}/>
            </div>
            
            
            <div>
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to={review.reviewId ? `/api/profile/${authContext.credentials.username}`: `/api/media`} className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default ReviewForm;