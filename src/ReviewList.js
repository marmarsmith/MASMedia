import Review from "./Review";
import { useState, useEffect, useContext } from "react";
import { useHistory, useParams } from "react-router";
import { findbyUsername } from "./services/review-api";
import AuthContext from "./contexts/AuthContext";

function ReviewList() {

    const [reviews, setReviews] = useState([]);
    const history = useHistory();
    const authContext = useContext(AuthContext);
    const { username } = useParams();

    useEffect(() => {
        findbyUsername(username)
            .then(result => setReviews(result))
            .catch((err) => {
                if (err === 403) {
                    err = "Unauthorized";
                    history.push(`/api/profile/${authContext.credentials.username}`, err.toString());
                } else {
                    history.push("/error", err.toString());
                }
            });
    }, [authContext, history, username]);

    return (
        <>
            <h2>{authContext.credentials.username == username ? `Welcome, ${authContext.credentials.username}!` : `${username}'s Profile`}</h2>
            <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-3">
                {reviews.map(r => <Review review={r} key={r.reviewId} />)}
            </div>
        </>
    );
}

export default ReviewList;