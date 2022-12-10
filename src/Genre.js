import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";

function Genre({ genre }) {

    const auth = useContext(AuthContext);

    return (
        <div className="col">
            <div className="card h-100">
            {"image?" && <img src={"image?"} className="card-img-top" alt={"image?"}></img>}
                <div className="card-body">
                    <h5 className="card-title">{genre.name}</h5>
                </div>
                {auth.credentials && auth.credentials.hasAuthority("ADMIN") &&
                    <div className="card-footer text-center">
                        {auth.credentials.hasAuthority("ADMIN") &&
                            <Link to={`/delete/${genre.genreId}`} className="btn btn-dark me-1">Delete</Link>}
                        <Link to={`/edit/${genre.genreId}`} className="btn btn-secondary">Edit</Link>
                    </div>
                }
            </div>
        </div>
    );
}

export default Genre;