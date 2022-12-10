import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../contexts/AuthContext";
import logo from "../MASmediaIII.png";

import { findAll } from "../services/media-api";

function Navigation() {

    const auth = useContext(AuthContext);
    const { credentials } = auth;

    return (
        <nav className="navbar sticky-top navbar-light bg-dark bg-gradient mb-0 pb-0" style={{ backgroundColor: "#dddddd" }}>
            <div className="container-fluid">

                <div style={{width: "90px", height: "90px"}}>
                <Link to="/api/media/" className="navbar-brand"><img src={logo} style={{width: "100%", height: "100%"}} alt="logo"></img></Link>
                </div>


                <ul className="nav">
                    {credentials && credentials.hasAuthority("ADMIN") &&
                        <li className="nav-item">
                            <Link to="/add" className="nav-link">Add Media</Link>
                        </li>
                    }
                    {credentials && credentials.hasAuthority("USER", "ADMIN") &&
                        <li className="nav-item">
                            <Link to={`/api/profile/${credentials.username}`}className="nav-link">Profile</Link>
                        </li>
                    }
                    {credentials && credentials.hasAuthority("USER", "ADMIN") &&
                        <li className="nav-item">
                            <Link to={`/user/privacy`}className="nav-link">Privacy</Link>
                        </li>
                    }
                    {credentials && credentials.hasAuthority("ADMIN") &&
                        <li className="nav-item">
                            <Link to="/users/manage" className="nav-link">Manage Users</Link>
                        </li>
                    }
                    {credentials ?
                        <>
                            <li className="nav-item">
                                <Link to="/password" className="nav-link">Change Password</Link>
                            </li>
                            <li className="nav-item">
                                <button className="btn btn-dark" onClick={auth.logout}>Log Out</button>
                            </li>
                        </>
                        :
                        <>
                            <li className="nav-item">
                                <Link to="/register" className="nav-link">Register</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="/login" className="nav-link">Login</Link>
                            </li>
                        </>}

                </ul>
            </div>
        </nav>
    );
}

export default Navigation;