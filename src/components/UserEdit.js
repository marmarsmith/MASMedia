import { useState, useEffect } from "react";
import { useParams, useHistory, Link } from "react-router-dom";
import { findRoles, findAppUserById, update } from "../services/user-api";

function UserEdit() {

    const [user, setUser] = useState({
        username: "",
        disabled: false,
        authorityNames: []
    });
    const [authorities, setAuthorities] = useState([]);
    const [err, setErr] = useState();
    const { appUserId } = useParams();
    const history = useHistory();

    if (!appUserId) {
        history.push("/users/manage");
    }

    useEffect(() => {
        Promise.all([findRoles(), findAppUserById(appUserId)])
            .then(([roles, u]) => {
                setAuthorities(roles);
                setUser(u);
            }).catch(() => history.push("/users/manage"));
    }, [appUserId, history]);

    const onChange = evt => {
        const clone = { ...user };
        if (evt.target.name === "authorities") {
            if (evt.target.checked) {
                clone.authorityNames.push(evt.target.value);
            } else {
                clone.authorityNames = clone.authorityNames.filter(a => a !== evt.target.value);
            }
        } else if (evt.target.type === "checkbox") {
            clone[evt.target.name] = evt.target.checked;
        } else {
            clone[evt.target.name] = evt.target.value;
        }
        setUser(clone);
    };

    const onSubmit = evt => {
        evt.preventDefault();
        update(user)
            .then(() => history.push("/users/manage"))
            .catch(err => {
                if (err.status === 400) {
                    setErr(err.messages[0]);
                } else {
                    console.log(err);
                    history.push("/error", err.toString());
                }
            });
    }

    return (
        <form onSubmit={onSubmit}>
            <h2>Edit User</h2>
            <div className="row mb-2 align-items-end">
                <div className="col">
                    <label className="form-label">Username</label>
                    <input type="text" id="username" name="username" className="form-control" required
                        value={user.username} onChange={onChange} />
                </div>
                <div className="col">
                    <div className="form-check">
                        <input className="form-check-input" type="checkbox" id="enabled" name="enabled"
                            checked={user.enabled} onChange={onChange} />
                        <label className="form-check-label" htmlFor="enabled">
                            Disable User
                        </label>
                    </div>
                </div>
            </div>

            <fieldset className="mb-3">
                <legend>Roles</legend>
                {authorities.map(a => (
                    <div className="form-check" key={a}>
                        <input className="form-check-input" type="checkbox" value={a} appUserId={`chk_${a}`} name="authorities"
                            checked={user.authorityNames.includes(a)} onChange={onChange} />
                        <label className="form-check-label" htmlFor={`chk_${a}`}>
                            {a}
                        </label>
                    </div>
                ))}
            </fieldset>
            {err && <div className="alert alert-dark">{err}</div>}
            <div className="mb-2">
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to="/users/manage" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default UserEdit;