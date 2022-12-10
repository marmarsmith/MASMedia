import { useState, useContext } from "react";
import { useHistory, Link } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";
import { changePassword } from "./services/user-api";

function ChangePassword() {

    const auth = useContext(AuthContext);
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [err, setErr] = useState();

    const history = useHistory();

    const onSubmit = (evt) => {
        evt.preventDefault();
        if (password !== confirmPassword) {
            setErr("passwords do not match");
        } else {
            changePassword(password)
                .then(() => history.push("/"))
                .catch(err => {
                    if (err.status === 400) {
                        setErr(err.messages[0]);
                    } else {
                        history.push("/error", err.toString());
                    }
                });
        }
    }

    return (
        <form onSubmit={onSubmit}>
            <h2>Change Password</h2>
            <div className="mb-2">
                <label className="form-label">Username</label>
                <span className="form-control">{auth.credentials.username}</span>
            </div>
            <div className="mb-2">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" id="password" name="password" className="form-control" required
                    value={password} onChange={evt => setPassword(evt.target.value)} />
            </div>
            <div className="mb-2">
                <label htmlFor="name" className="form-label">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" className="form-control" required
                    value={confirmPassword} onChange={evt => setConfirmPassword(evt.target.value)} />
            </div>
            {err && <div className="alert alert-dark">{err}</div>}
            <div className="mb-2">
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to="/" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default ChangePassword;