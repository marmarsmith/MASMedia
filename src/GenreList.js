import Genre from "./Genre";
import { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router";
import { findAll } from "./services/genre-api";
import AuthContext from "./contexts/AuthContext";

function GenreList() {

    const [genres, setGenres] = useState([]);
    const history = useHistory();
    const authContext = useContext(AuthContext);

    useEffect(() => {
        findAll()
            .then(result => setGenres(result))
            .catch((err) => {
                if (err === 403) {
                    authContext.logout();
                    history.push("/login");
                } else {
                    history.push("/error", err.toString());
                }
            });
    }, [authContext, history]);

    return (
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-3">
            {genres.map(g => <Genre genre={g} key={g.genreId} />)}
        </div>
    );
}

export default GenreList;