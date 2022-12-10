import { useState, useEffect, useContext, useCallback } from "react";
import { Link, useHistory } from "react-router-dom";
import AuthContext from "../contexts/AuthContext";
import { update, findByUsername, changePassword, changePrivacy } from "../services/user-api";

// function UserPrivacy() {

//     const [user, setUser] = useState({
//         username: "",
//         privateProfile: false
//     });

//     const [err, setErr] = useState();
//     const history = useHistory();
//     const authContext = useContext(AuthContext);

//     const handleErr = useCallback(err => {
//         if (err === 403) {
//             authContext.logout();
//             err = "Unauthorized";
//         }
//         history.push("/error", err.toString())
//     }, [authContext, history]);

//     useEffect(() => {
//         if (authContext.credentials.username) {
//             findByUsername(authContext.credentials.username)
//             .then(result => setUser(result))
//             .catch(handleErr);
//         }

//     }, [history, authContext.credentials.username, handleErr]);

//     const onChange = evt => {
//         const clone = { ...user };

//         if (evt.target.type === "checkbox") {
//             clone[evt.target.name] = evt.target.checked;
//         } else {
//             clone[evt.target.name] = evt.target.value;
//         }
//         setUser(clone);
//     };

//     const onSubmit = evt => {
//         evt.preventDefault();
//         update(user)
//             .then(() => history.push("/user/privacy"))
//             .catch(err => {
//                 if (err.status === 400) {
//                     setErr(err.messages[0]);
//                 } else {
//                     console.log(err);
//                     history.push("/error", err.toString());
//                 }
//             });
//     }

//     return (
//         <form onSubmit={onSubmit}>
//             <h2>Would You like Your Profile to be Private?</h2>
//             <div className="row mb-2 align-items-end">
//                 <div className="col">
//                     <label className="form-label">{user.username}</label>
//                 </div>
//                 <div className="col">
//                     <div className="form-check">
//                         <input className="form-check-input" type="checkbox" id="privateProfile" name="privateProfile"
//                             checked={user.privateProfile} onChange={onChange} />
//                         <label className="form-check-label" htmlFor="privateProfile">
//                             Private Profile
//                         </label>
//                     </div>
//                 </div>
//             </div>
//             {err && <div className="alert alert-danger">{err}</div>}
//             {authContext.credentials && authContext.credentials.hasAuthority("USER", "ADMIN") &&
//             <div className="mb-2">
//                 <button type="submit" className="btn btn-primary me-1">Save</button>
//                 <Link to={`/api/profile/${user.username}`} className="btn btn-secondary">Cancel</Link>
//             </div>
//     }
//         </form>
//     );
//  }

// export default UserPrivacy;

function UserPrivacy() {

    const authContext = useContext(AuthContext);
    // const [privateProfile, setPrivateProfile] = useState(false);
    const [user, setUser] = useState({
                username: "",
                privateProfile: false
            });
    const history = useHistory();

    const handleErr = useCallback(err => {
        if (err === 403) {
            err = "Unauthorized";
        }
        history.push(`/api/media`, err.toString())
    }, [authContext, history]);

    useEffect(() => {
        if (authContext.credentials.username) {
            findByUsername(authContext.credentials.username)
                .then(result => {
                    setUser(result)
                })
                .catch(handleErr);
        }
    }, [authContext.credentials.username, handleErr]);

    const onChange = evt => {
        const clone = { ...user };

        if (evt.target.type === "checkbox") {
            clone[evt.target.name] = evt.target.checked;
        } else {
            clone[evt.target.name] = evt.target.value;
        }
        setUser(clone);
    };

    const onSubmit = (evt) => {
        evt.preventDefault();
        changePrivacy({ appUserId: user.appUserId, username: authContext.credentials.username, privateProfile: user.privateProfile })
            .then(() => history.push(`/api/profile/${authContext.credentials.username}`))
            .catch(handleErr);
    }

    return (
        <form onSubmit={onSubmit}>
            <h2>Privacy Settings</h2>
            <h3>Would you like your profile to be private?</h3>
            <div className="row mb-2 align-items-end">
                <div className="col">
                    <label className="form-label">{authContext.credentials.username}</label>
                </div>
                <div className="col">
                    <div className="form-check">
                        <input className="form-check-input" type="checkbox" id="privateProfile" name="privateProfile"
                            checked={user.privateProfile} onChange={onChange} />
                        <label className="form-check-label" htmlFor="privateProfile">
                            Private Profile
                        </label>
                    </div>
                </div>
            </div>
            {/* {err && <div className="alert alert-danger">{err}</div>} */}
            {authContext.credentials && authContext.credentials.hasAuthority("USER", "ADMIN") &&
                <div className="mb-2">
                    <button type="submit" className="btn btn-primary me-1">Save</button>
                    <Link to={`/api/profile/${user.username}`} className="btn btn-secondary">Cancel</Link>
                </div>
            }
        </form>
    );
}

export default UserPrivacy;