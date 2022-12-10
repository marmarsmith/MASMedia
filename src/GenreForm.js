import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, save } from "./services/genre-api";
import AuthContext from "./contexts/AuthContext";

const emptyGenre = {
    name: ""
};

function GenreForm() {

    const [genre, setGenre] = useState(emptyGenre);
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
        }
    }, [genreId, history, handleErr]);

    const onChange = evt => {
        const next = { ...genre };
        let value = evt.target.value;
        if (evt.target.type === "number") {
            value = parseInt(value, 10);
            if (isNaN(value)) {
                value = evt.target.value;
            }
        }
        next[evt.target.name] = value;
        setGenre(next);
    };

    const onSubmit = evt => {
        evt.preventDefault();
        save(genre)
            .then(() => history.push("/api/genre"))
            .catch(handleErr);
    };

    return (
        <form onSubmit={onSubmit}>
            <h2>{genre.genreId ? "Update" : "Add"} a Genre</h2>
            <div className="mb-2">
                <label htmlFor="name" className="form-label">Genre</label>
                <input type="text" id="name" name="name" className="form-control" required
                    value={genre.name} onChange={onChange} />
            </div>
            <div className="mb-2">
                <label htmlFor="image?" className="form-label">Add Image?</label>
                <input type="url" id="image?" name="image?" className="form-control"
                    value={"image?"} onChange={onChange} />
            </div>

            <div>
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to="/api/genre" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default GenreForm;