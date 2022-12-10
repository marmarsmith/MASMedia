import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";
import React from 'react';
import Stars from './components/Stars';

function Media({ media }) {

    const auth = useContext(AuthContext);
    //const mediaLink = getElementById()

    return (
        <div className="col">
            <div className="card h-100 bg-light">
                {"image?" && <img src={media.imageUrl} className="card-img-top" alt={"image?"}></img>}
                <div className="card-body">
                    <h5 className="card-title"><strong>{media.title}</strong></h5>
                    <div className="row">
                        <div></div>
                        <div className="col"><strong>Release Year:</strong> </div>
                        <div></div>
                        <div className="col">{media.year}</div>
                    </div>
                    <div className="row">
                        <div className="col"><strong>Type:</strong> </div>
                        <div></div>
                        <div className="col">{media.type}</div>
                    </div>
                    <div className="row">
                        <div className="col"><strong>Overview:</strong> </div>
                        <div></div>
                        <div className="col"><a href={media.link}>Click here for overview.</a></div>
                    </div>
                    <div className="row">
                        <div className="col"><strong>Online Rating:</strong> </div>
                        <div></div>
                        <div className="col"><Stars stars={media.mean_stars} edit={false}/></div>
                    </div>
                    {/* <div className="row">
                    <div className="col"><strong>Genre:</strong> </div>
                        
                        
                        <div className="col">{media.genres.map((genre, index) => {
                            return(<span key={genre.genreId}>{genre.name}{index !== media.genres.length -1 && ", "}</span>)
                        })}</div>
                    </div> */}
                
                </div>
                {auth.credentials && auth.credentials.hasAuthority("USER", "ADMIN") &&
                    <div className="card-footer text-center">
                        <Link to={`/review/${media.mediaId}`} className="btn btn-info me-1 btn-sm">Reviews</Link>
                        {auth.credentials.hasAuthority("ADMIN") &&
                        <>
                        <Link to={`/delete/${media.mediaId}`} className="btn btn-dark me-1 btn-sm">Delete</Link>
                        <Link to={`/edit/${media.mediaId}`} className="btn btn-secondary me-1 btn-sm">Edit</Link>
                        </>}
                        
                    </div>
                }
            </div>
        </div>
    );
}

export default Media;