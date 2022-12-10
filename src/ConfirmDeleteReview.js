import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, deleteById } from "./services/review-api";
import AuthContext from "./contexts/AuthContext";

function ConfirmDeleteReview() {

    const [review, setReview] = useState();
    const { username, reviewId } = useParams();
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
        if (reviewId) {
            findById(username, reviewId)
                .then(result => setReview(result))
                .catch(handleErr);
        } else {
            history.push("/error", "Could not find review.");
        }
    }, [username, reviewId, history, handleErr]);

    const onDelete = () => {
        deleteById(review.user.username, review.reviewId)
            .then(() => history.push(`/api/profile/${username}`))
            .catch(handleErr);
    }

    return (
        <div>
            {review ? <><h2>Delete Review for {review.media.title}?</h2>
                <div className="alert alert-dark">
                You will completely delete the review for {review.media.title}.<br />
                    Are you sure?
                </div>
                <div className="mt-2">
                    <button className="btn btn-dark me-1" onClick={onDelete}>Yes, Delete It!</button>
                    <Link to={`/api/profile/${username}`} className="btn btn-secondary">No, Cancel</Link>
                </div></> : <img src="https://i.redd.it/o6m7b0l6h6pz.gif" alt="" />
            }
        </div>
    );
}

export default ConfirmDeleteReview;