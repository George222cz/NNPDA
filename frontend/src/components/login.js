import React from "react";
import {useState} from "react";
import { useHistory } from "react-router-dom";

import AuthService from "../services/auth";
import UserService from "../services/user";

export default function Login() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [loading, setLoading] = useState(false)
    const [feedback, setFeedback] = useState(undefined)
    let history = useHistory();

    const onSubmitHandler = event => {
        event.preventDefault();
        setLoading(true);
        setFeedback(undefined);

        const loginRequest = {
            username: username,
            password: password
        }

        UserService.postDataAPI('auth/signin',loginRequest).then(json => {
            if (json.accessToken) {
                AuthService.login(json);
            }
            return json;
        }).then(() => {
            history.push("/profile");
            window.location.reload();
        }).catch((error)=>{
            setFeedback(error.message.length<50 ? error.message:"Error: "+JSON.parse(error.message).message);
            setLoading(false);
        });
    }

    return (
        <div>
            <h2>Sign in</h2><br/>
            <div className={"container"} style={{width: "25%", paddingTop: "20px"}}>
                <form onSubmit={onSubmitHandler}>
                    <input type={"text"} placeholder={"Username"} id={"username"} required={true} value={username} onChange={(e) => setUsername(e.target.value)}/>
                    <input type={"password"} placeholder={"Password"} id={"password"} required={true} value={password} onChange={(e) => setPassword(e.target.value)} style={{marginTop: "6px"}}/>
                    <input type={"submit"} value={"Sign in"} className={"submitButton"}/>
                </form>
                <div>
                {loading && "Loading..."}
                {feedback && <div id={"unauthorized"}>{feedback}</div>}
                </div>
            </div>
        </div>
    );

}