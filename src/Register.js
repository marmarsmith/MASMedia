import { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { create } from "./services/user-api";

function Register() {

    const [user, setUser] = useState({
        username: "",
        password: "",
        confirmPassword: ""
    });
    const [err, setErr] = useState();

    const history = useHistory();

    const onChange = (evt) => {
        const clone = { ...user };
        clone[evt.target.name] = evt.target.value;
        setUser(clone);
    };

    const onSubmit = (evt) => {
        evt.preventDefault();
        if (user.password !== user.confirmPassword) {
            setErr("passwords do not match");
        } else {
            create(user)
                .then(() => history.push("/login"))
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
            <h2>Register</h2>
            <div className="mb-2">
                <label htmlFor="username" className="form-label">Username</label>
                <input type="text" id="username" name="username" className="form-control" required
                    value={user.username} onChange={onChange} />
            </div>
            <div className="mb-2">
                <label htmlFor="name" className="form-label">Password</label>
                <input type="password" id="password" name="password" className="form-control" required
                    value={user.password} onChange={onChange} />
            </div>
            <div className="mb-2">
                <label htmlFor="name" className="form-label">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" className="form-control" required
                    value={user.confirmPassword} onChange={onChange} />
            </div>
            {err && <div className="alert alert-dark">{err}</div>}
            <div className="mb-2">
                <button type="submit" className="btn btn-primary me-1">Save</button>
                <Link to="/" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
    );
}

export default Register;