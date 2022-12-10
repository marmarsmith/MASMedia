import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useParams, useHistory } from "react-router-dom";
import { findById, deleteById } from "./services/media-api";
import AuthContext from "./contexts/AuthContext";

function ConfirmDeleteMedia() {

    const [media, setMedia] = useState();
    const { mediaId} = useParams();
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
                .then(result => setMedia(result))
                .catch(handleErr);
        } else {
            history.push("/error", "Could not find media.");
        }
    }, [mediaId, history, handleErr]);

    const onDelete = () => {
        deleteById(media.mediaId)
            .then(() => history.push("/api/media"))
            .catch(handleErr);
    }

    return (
        <div>
            {media ? <><h2>Delete {media.title} - {media.type} - {media.year}?</h2>
                <div className="alert alert-dark">
                    This will completely delete {media.title} - {media.type} - {media.year}.<br />
                    Are you sure?
                </div>
                <div className="mt-2">
                    <button className="btn btn-dark me-1" onClick={onDelete}>Yes, Delete It!</button>
                    <Link to="/api/media" className="btn btn-secondary">No, Cancel</Link>
                </div></> : <img src="https://i.redd.it/o6m7b0l6h6pz.gif" alt="" />
            }
        </div>
    );
}

export default ConfirmDeleteMedia;