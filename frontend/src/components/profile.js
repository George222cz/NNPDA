import React, {useEffect} from "react";
import {useState} from "react";
import {Redirect} from "react-router-dom";

import AuthService from "../services/auth";

export default function Profile() {

    const [redirect, setRedirect] = useState(undefined)
    const [currentUser, setCurrentUser] = useState(undefined)

    useEffect(()=>{
        const user = AuthService.getCurrentUser();
        if(!user) setRedirect("/home");
        setCurrentUser(user);
    },[]);

    return (
        <div>
            {redirect && <Redirect to={redirect} />}
            {currentUser && <div>
                <h2 id={"welcome"}>Welcome {currentUser.username}</h2><br/>
                <p>Email: {currentUser.email}</p>
                <p>Phone: {currentUser.phone}</p>
                <p>Role: {currentUser.roles[0]}</p>
            </div>}

        </div>
    );
}