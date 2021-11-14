import './App.css';
import React, {useEffect, useState} from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import Home from "./components/home";
import Login from "./components/login";
import AuthService from "./services/auth";
import Profile from "./components/profile";
import Devices from "./components/devices";
import Sensors from "./components/sensors";
import SensorForm from "./components/sensor-form";
import NotFound from "./components/not-found";
import Registration from "./components/registration";
import Measurements from "./components/measurements";

function App() {

    const [showMenu, setShowMenu] = useState(false);
    const [currentUser, setCurrentUser] = useState(undefined);

    useEffect(()=>{
        const user = AuthService.getCurrentUser();
        if(user){
            setCurrentUser(user);
        }
    },[])

    const logOut = function () {
        AuthService.logout();
    }

    return (
    <Router>
        <div className="App">
            <nav className="navbar">
                <button className="navbar-toggler" type="button" id={"navbar-toggler"} onClick={()=>setShowMenu(!showMenu)}>&#9776;</button>
                <div className="navbar-collapse" id={showMenu ? "hiddenMenu":""}>
                    <ul>
                        <li>
                            <Link to="/">Home</Link>
                        </li>
                        {!currentUser ? (
                            <>
                            <li>
                                <Link to="/login">Sign in</Link>
                            </li>
                            <li>
                                <Link to="/register">Sign up</Link>
                            </li>
                            </>
                        ) : (
                            <>
                            {currentUser.roles.some(r=>["ROLE_USER","ROLE_ADMIN"].includes(r)) && <li><Link to="/devices">Devices</Link></li>}
                            {currentUser.roles.some(r=>["ROLE_USER","ROLE_ADMIN"].includes(r)) && <li><Link to="/sensors">Sensors</Link></li>}
                            <li style={{float: "right"}}>
                                <a href="/" onClick={logOut}>Logout</a>
                            </li>
                            <li style={{float: "right"}}>
                                <Link to="/profile">Profile</Link>
                            </li>
                            </>
                        )}
                    </ul>
                </div>
            </nav>
            <Switch>
                <Route exact path={["/", "/home"]} component={Home} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/register" component={Registration} />
                <Route exact path="/profile" component={Profile} />
                <Route exact path="/devices" component={Devices} />
                <Route path="/sensors/:deviceId" component={Sensors} />
                <Route path="/sensors" component={Sensors} />
                <Route path="/sensor/:sensorId" component={SensorForm} />
                <Route path="/sensor-form/:deviceId" component={SensorForm} />
                <Route path="/measurements/:sensorId" component={Measurements} />
                <Route component={NotFound} />
            </Switch>
        </div>
    </Router>
  );
}

export default App;
