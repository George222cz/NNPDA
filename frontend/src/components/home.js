import React from "react";

export default function Home() {

    return(
        <div>
            <h1>Measurements</h1>
            <div className={"container"} style={{maxWidth: "87%", fontSize: "1.2em"}}>
                &nbsp;&nbsp;&nbsp; Welcome to measurement app administration... <a href={"http://localhost:5601"}>Click here for Kibana view.</a><br/>
                ⬆ &nbsp;Please use the navigation bar to navigate the app.

            </div>
            <br/>
        </div>
    )
}