import Media from "./Media";
import { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router";
import { findAll } from "./services/media-api";
import AuthContext from "./contexts/AuthContext";
import SearchBar from "./components/SearchBar";


function MediaList() {

    const [medias, setMedias] = useState([]);
    const history = useHistory();
    const authContext = useContext(AuthContext);

    useEffect(() => {
        findAll()
            .then(result => setMedias(result))
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
        <div className = "searchbar">
            <SearchBar placeholder = "Write something to search for" data={medias}/>
           
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-3">
            {medias.map(m => <Media media={m} key={m.mediaId} />)} 
        </div>
        </div>
    );
}

export default MediaList;