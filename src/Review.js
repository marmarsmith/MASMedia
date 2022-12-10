import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";
import React from 'react';
import Stars from './components/Stars';



function Review({ review }) {

    const auth = useContext(AuthContext);

    return (
        <div className="col">
            <div className="card h-100">
                <div className="card-body">
                    <h5 className="card-title"><strong>{review.media.title}</strong></h5>
                    <div></div>
                    <div className="row">
                        <div className="col"><strong>Posted By:</strong> </div>
                        <div></div>
                        <div className="col"><Link to={`/api/profile/${review.user.username}`}>{review.user.username}</Link></div>
                    </div>
                    <div className="row">
                        <div className="col"><strong>Rating:</strong> </div>
                        <div></div>
                        <div className="col"><Stars stars={review.stars} edit={false}/></div>
                    </div>
                    <div className="row">
                        <div className="col"><strong>Opinion:</strong> </div>
                        <div></div>
                        <div className="col">{review.opinion}</div>
                    </div>
                    
                </div>

                {auth.credentials && auth.credentials.hasAuthority("USER", "ADMIN") &&
                    <div className="card-footer text-center">
                        {(auth.credentials.hasAuthority("ADMIN") || auth.credentials.username == review.user.username) &&
                            <Link to={`/api/profile/${review.user.username}/${review.reviewId}/delete`}className="btn btn-dark me-1">Delete</Link>}
                        {(auth.credentials.username == review.user.username) &&
                        <Link to={`/api/profile/${review.user.username}/${review.reviewId}`} className="btn btn-secondary">Edit</Link>}
                    </div>
                }
            </div>
        </div>
    );
}

export default Review;