import Review from "./Review";
import { useState, useEffect, useContext } from "react";
import { useHistory, useParams } from "react-router";
import { findByMediaId } from "./services/review-api";
import AuthContext from "./contexts/AuthContext";
import { Link } from "react-router-dom";
import {findById } from "./services/media-api";

function ReviewSearch() {

    const [reviews, setReviews] = useState([]);
    const history = useHistory();
    const authContext = useContext(AuthContext);
    const { mediaId } = useParams();
    const [media, setMedia] = useState([]);

    useEffect(() => {
        findById(mediaId).then(result => setMedia(result));
        findByMediaId(mediaId)
            .then(result => setReviews(result))
            .catch((err) => {
                if (err === 403) {
                    authContext.logout();
                    history.push("/login");
                } else {
                    history.push("/error", err.toString());
                }
            });
    }, [authContext, history, mediaId]);

    return (
        <>
            <h2>{media.title} Reviews</h2>
            {authContext.credentials && authContext.credentials.hasAuthority("USER", "ADMIN") &&
                    <div>
                        <Link to={`media/${mediaId}`} className="btn btn-info">Add Review</Link>
                    </div>
                }
                <br></br>
            <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-3">
                {reviews.map(r => <Review review={r} key={r.reviewId} />)}
            </div>
        </>
    );

}

export default ReviewSearch;