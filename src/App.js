import { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect
} from "react-router-dom";
import MediaList from "./MediaList";
import MediaForm from "./MediaForm";
import GenreList from "./GenreList";
import GenreForm from "./GenreForm";
import ReviewList from "./ReviewList";
import ReviewForm from "./ReviewForm";
import ReviewSearch from "./ReviewSearch";
import ChangePassword from "./ChangePassword";
import ConfirmDeleteMedia from "./ConfirmDeleteMedia";
import ConfirmDeleteGenre from "./ConfirmDeleteGenre";
import ConfirmDeleteReview from "./ConfirmDeleteReview";
import UserPrivacy from "./components/UserPrivacy";

// import UserDelete from "./components/UserDelete"
// import Search from "./components/Search";
import Login from "./Login";
import Register from "./Register";
import { Error, Navigation, NotFound, UserEdit, Users } from "./components";
import AuthContext from "./contexts/AuthContext";
import { logout, refresh } from "./services/auth-api";

function App() {

  const [credentials, setCredentials] = useState();
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    refresh()
      .then(principal => setCredentials(principal))
      .catch(() => setCredentials())
      .finally(() => setInitialized(true));
  }, [])

  const auth = {
    credentials,
    login: (principal) => setCredentials(principal),
    logout: () => {
      logout().finally(() => setCredentials());
    }
  };

  const considerRedirect = (Component, ...authorities) => {
    if (initialized) {

      if (credentials && (authorities.length === 0 || credentials.hasAuthority(...authorities))) {
        return <Component />;
      } else {
        return <Redirect to="/login" />;
      }
    }
    return null;
  };

  return (
    <AuthContext.Provider value={auth}>
      <Router>
        <Navigation />
        <div className="bg-dark mt-0 pt-0">
        <div className="container mt-0 mb-5">

          <Switch>

            <Route exact path={["/review/:mediaId"]}>
              {considerRedirect(ReviewSearch, "USER", "ADMIN")}
            </Route>
            <Route path={["/api/review/id/:reviewId", "/review/media/:mediaId"]}>
              {considerRedirect(ReviewForm, "USER", "ADMIN")}
            </Route>
            <Route exact path="/api/media">
              {considerRedirect(MediaList)}
            </Route>
            <Route path={["/edit/:mediaId", "/add"]}>
              {considerRedirect(MediaForm, "ADMIN")}
            </Route>
            <Route exact path="/api/profile/:username/:reviewId/delete">
              {considerRedirect(ConfirmDeleteReview, "USER", "ADMIN")}
            </Route>
            <Route exact path="/api/profile/:username/:reviewId">
              {considerRedirect(ReviewForm, "USER", "ADMIN")}
            </Route>
            <Route exact path="/api/profile/:username">
              {considerRedirect(ReviewList, "USER", "ADMIN")}
            </Route>
            {/* <Route path={["/api/media/search/:genreList"]}>
              {considerRedirect(Search, "USER", "ADMIN")}
            </Route> */}
            <Route path={"/api/media/:mediaId"}>
              {considerRedirect(MediaForm, "USER", "ADMIN")}
            </Route>
            <Route exact path="/api/genre">
              {considerRedirect(GenreList)}
            </Route>
            <Route path={["/edit/:genreId", "/add"]}>
              {considerRedirect(GenreForm, "ADMIN")}
            </Route>
            <Route path={["/user/privacy"]}>
              {considerRedirect(UserPrivacy, "USER", "ADMIN")}
            </Route>
            {/* <Route path={["/user/delete"]}>
              {considerRedirect(UserDelete, "USER", "ADMIN")}
            </Route> */}
            {/* Some unedited potentially useful links */}
            <Route path="/delete/:mediaId">
              {considerRedirect(ConfirmDeleteMedia, "ADMIN")}
            </Route>
            <Route path="/delete/:genreId">
              {considerRedirect(ConfirmDeleteGenre, "ADMIN")}
            </Route>
            <Route path="/users/manage">
              {considerRedirect(Users, "ADMIN")}
            </Route>
            <Route path="/user/edit/:id">
              {considerRedirect(UserEdit, "ADMIN")}
            </Route>
            <Route path="/register">
              <Register />
            </Route>
            <Route path="/login">
              <Login />
            </Route>
            <Route path="/password">
              {considerRedirect(ChangePassword)}
            </Route>
            <Route path="/error">
              <Error />
            </Route>
            <Route>
              <NotFound />
            </Route>
          </Switch>

        </div>
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
