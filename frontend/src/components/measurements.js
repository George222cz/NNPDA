import React, {useEffect} from "react";
import {useState} from "react";
import UserService from "../services/user";

export default function Measurements(props) {

    const [content, setContent] = useState(undefined)
    const [error, setError] = useState(undefined)
    const [loading, setLoading] = useState(false)

    useEffect(()=>{
        setLoading(true);
        UserService.getDataAPI('measurement',"sensor/"+props.match.params.sensorId).then(json=>{
            setContent(json);
            setLoading(false);
        }).catch((error)=>{
            setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    },[]);

    return (
        <div>
            <h2>Measurements of sensor id:{props.match.params.sensorId}</h2>
            {loading ? "Loading...": <br/>}
            {error && <h3 style={{color: "red"}}>{error}</h3>}
            <div className={"container"}>
                {content && (content.length===0 ? <div>Empty</div> : <div style={{marginBottom: "10px"}}><table style={{width:"100%"}}>
                    <thead><tr><td>Id</td><td>Time</td><td>Value</td></tr></thead>
                    <tbody>{content.map((measurement,index)=>
                        <tr key={index} className={"row"}>
                            <td>{measurement.id}</td>
                            <td>{measurement.timeOfMeasurement}</td>
                            <td>{measurement.value}</td>
                        </tr>)}
                    </tbody>
                </table></div>)}
            </div>
        </div>
    );
}