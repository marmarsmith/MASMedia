import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, deleteById } from "./services/genre-api";
import AuthContext from "./contexts/AuthContext";

function ConfirmDeleteGenre() {

    const [genre, setGenre] = useState();
    const { genreId } = useParams();
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
        if (genreId) {
            findById(genreId)
                .then(result => setGenre(result))
                .catch(handleErr);
        } else {
            history.push("/error", "Could not find genre.");
        }
    }, [genreId, history, handleErr]);

    const onDelete = () => {
        deleteById(genre.genreId)
            .then(() => history.push("/api/genre"))
            .catch(handleErr);
    }

    return (
        <div>
            {genre ? <><h2>Delete {genre.name}?</h2>
                <div className="alert alert-dark">
                    This will completely delete {genre.name}.<br />
                    Are you sure?
                </div>
                <div className="mt-2">
                    <button className="btn btn-dark me-1" onClick={onDelete}>Yes, Delete It!</button>
                    <Link to="/api/genre" className="btn btn-secondary">No, Cancel</Link>
                </div></> : <img src="https://i.redd.it/o6m7b0l6h6pz.gif" alt="" />
            }
        </div>
    );
}

export default ConfirmDeleteGenre;