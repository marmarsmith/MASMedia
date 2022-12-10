import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, save } from "./services/media-api";
import AuthContext from "./contexts/AuthContext";
import React from 'react';
import Stars from './components/Stars';

const emptyMedia = {
    title: "",
    year: "",
    type: "",
    link: "",
    imageUurl: "",
    mean_stars: "",
};

function MediaForm() {

    const [mean_stars, setStars] = useState(0);

    const ratingChanged = (newRating) => {
      console.log(newRating)
      setStars(newRating)
    }
    
    const [media, setMedia] = useState(emptyMedia);
    const { mediaId } = useParams();
    const history = useHistory();
    const authContext = useContext(AuthContext);

    

    const handleErr = useCallback(err => {
        if (err === 403) {
            authContext.logout();
            err = "Unauthorized";
        }
        history.push("/error", err.toString())
    }, [authContext, history]);

    useEffect(() => {
        if (mediaId) {
            findById(mediaId)
            .then(result => {
                setMedia(result)
                setStars(result.mean_stars)
            })
                .catch(handleErr);
        }
    }, [mediaId, history, handleErr]);

    const handleChange = evt => {
        const next = { ...media };
        let value = evt.target.value;
        
        next[evt.target.name] = value;
        setMedia(next);
    };

    const onSubmit = evt => {
        evt.preventDefault();
        save({...media, mean_stars})
            .then(() => history.push("/api/media"))
            .catch(handleErr);
    };

    return (
        <form onSubmit={onSubmit}>
            <h2>{media.mediaId ? "Update" : "Add"} a Media</h2>
            <div className="mb-2">
                <label htmlFor="title" className="form-label">Title</label>
                <input type="text" id="title" name="title" className="form-control" required
                    value={media.title} onChange={handleChange} />
            </div>
            <div className="col">
                <div className="col">
                    <label htmlFor="year" className="form-label">Release Year</label>
                    <input type="number" id="year" name="year" className="form-control" required
                        value={media.year} onChange={handleChange} />
                </div>
            </div>
            <div className="mb-2">
                <label htmlFor="type" className="form-label">Media Type</label>
                <select id="type" name="type" className="form-control" required
                    value={media.type} onChange={handleChange}>
                    <option value=""> [Choose] </option>
                    <option>BOOK</option>
                    <option>MOVIE</option>
                    <option>TV_SHOW</option>
                </select>
            </div>
            <div className="mb-2">
                <label htmlFor="link" className="form-label">Overview</label>
                <input type="text" id="link" name="link" className="form-control"
                    value={media.link} onChange={handleChange} />
            </div>
            <div className="mb-2">
                <label htmlFor="imageUrl" className="form-label">Image Url</label>
                <input type="text" id="imageUrl" name="imageUrl" className="form-control"
                    value={media.imageUrl} onChange={handleChange} />
            </div>
            
            <div id="stars" className="col">
                <label htmlFor="mean_stars" className="form-label">Online Rating</label>
                <Stars stars={mean_stars} ratingChanged={ratingChanged} edit={true}/>
            </div>
            


            

            <div class="buttons">
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to="/api/media" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default MediaForm;