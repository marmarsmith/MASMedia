import { useState, useContext } from "react";
import { Link, useHistory } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";
import { login } from "./services/auth-api";

function Login() {

    const [candidate, setCandidate] = useState({
        username: "",
        password: ""
    });
    const [hasError, setHasError] = useState(false);

    const history = useHistory();
    const authContext = useContext(AuthContext);

    // event handlers
    const onChange = (evt) => {
        const clone = { ...candidate };
        clone[evt.target.name] = evt.target.value;
        setCandidate(clone);
    }

    const onSubmit = (evt) => {
        evt.preventDefault();
        login(candidate)
            .then(principal => {
                authContext.login(principal);
                history.push("/api/media");
            }).catch(() => setHasError(true));
    }

    return (
        <form onSubmit={onSubmit}>
            <h2>Login</h2>
            <div className="mb-2">
                <label htmlFor="username" className="form-label">Username</label>
                <input type="text" id="username" name="username" className="form-control" required
                    value={candidate.username} onChange={onChange} />
            </div>
            <div className="mb-2">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" id="password" name="password" className="form-control" required
                    value={candidate.password} onChange={onChange} />
            </div>
            <div>
                <Link to="/api/media" className="btn btn-secondary me-2">Cancel</Link>
                <button type="submit" className="btn btn-primary">Log In</button>
            </div>
            {hasError && <div className="alert alert-dark">Username and/or password is incorrect.</div>}
        </form>
    );
}

export default Login;