import React from "react";
import {useState} from "react";
import { useHistory } from "react-router-dom";

import UserService from "../services/user";

export default function Registration() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [email, setEmail] = useState("")
    const [phone, setPhone] = useState("")
    const [role, setRole] = useState("ROLE_USER")
    const [loading, setLoading] = useState(false)
    const [feedback, setFeedback] = useState(undefined)
    let history = useHistory();

    const onSubmitHandler = event => {
        event.preventDefault();
        setLoading(true);
        setFeedback(undefined);

        const registerRequest = {
            username: username,
            password: password,
            email: email,
            phone: phone,
            role: role
        }

        UserService.postDataAPI('auth/signup',registerRequest,"",false,false).then(json => {
            setFeedback(json);
            setLoading(false);
            return json;
        }).then(() => {
            setTimeout(() => {
                history.push("/login");
                window.location.reload();
            }, 1500);
        }).catch((error)=>{
            setFeedback(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    }

    return (
        <div>
            <h2>Sign up</h2><br/>
            <div className={"container"} style={{width: "25%", paddingTop: "20px"}}>
                <form onSubmit={onSubmitHandler}>
                    <label htmlFor={"Username"}>Username: </label>
                    <input type={"text"} placeholder={"Username"} id={"Username"} required={true} value={username} onChange={(e) => setUsername(e.target.value)}/>
                    <label htmlFor={"Password"}>Password: </label>
                    <input type={"password"} placeholder={"Password"} id={"Password"} required={true} value={password} onChange={(e) => setPassword(e.target.value)}/>
                    <label htmlFor={"Email"}>Email: </label>
                    <input type={"text"} placeholder={"Email"} id={"Email"} required={true} value={email} onChange={(e) => setEmail(e.target.value)}/>
                    <label htmlFor={"Phone"}>Phone number: </label>
                    <input type={"text"} placeholder={"Phone number"} id={"Phone"} required={true} value={phone} onChange={(e) => setPhone(e.target.value)}/>
                    <label htmlFor={"Role"}>Role: </label>
                    <select onChange={(e)=>setRole(e.target.value)}>
                        <option value="ROLE_USER">USER</option>
                        <option value="ROLE_ADMIN">ADMIN</option>
                    </select>
                    <input type={"submit"} value={"Sign up!"} className={"submitButton"}/>
                </form>
                <div>
                    {loading && "Loading..."}
                    {feedback && <div>{feedback}</div>}
                </div>
            </div>
        </div>
    );
}