import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { findAll } from "../services/user-api";

function Users() {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        findAll()
            .then(setUsers)
    }, []);

    return (
        <>
            <h2>Manage Users</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th className="text-center">Private?</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.appUserId}>
                            <td>{user.username}</td>
                            <td className="text-center">{user.privateProfile ? "Yes" : "No"}</td>
                            <td><Link to={`/api/profile/${user.username}`} className="btn btn-secondary">Profile</Link></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </>
    );
}

export default Users;