import React, {useEffect} from "react";
import {useState} from "react";
import UserService from "../services/user";
import {Link} from "react-router-dom";

export default function Sensors(props) {

    const [content, setContent] = useState(undefined)
    const [error, setError] = useState(undefined)
    const [loading, setLoading] = useState(false)
    const [deviceId, setDeviceId] = useState(undefined)
    const [deviceContent, setDeviceContent] = useState(undefined)

    useEffect(()=>{
        setLoading(true);
        let optionalURL = "user/"+UserService.getUser().id;

        if(props.match.params.deviceId){
            optionalURL = "device/"+props.match.params.deviceId;
            setDeviceId(props.match.params.deviceId);
        }else{
            UserService.getDataAPI('device',UserService.getUser().id).then(json=>{
                setDeviceContent(json);
                if(json && Object.keys(json).length>0) setDeviceId(json[0].id);
                setLoading(false);
            }).catch((error)=>{
                setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
                setLoading(false);
            });
        }

        UserService.getDataAPI('sensor',optionalURL).then(json=>{
            setContent(json);
            setLoading(false);
        }).catch((error)=>{
            setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    },[]);

    const handleRemoveSensor = id => {
        if(window.confirm("Do you really want remove this sensor?")){
            setLoading(true);
            UserService.deleteDataAPI('sensor',undefined,id).then(json=>{
                setLoading(false);
                setContent(json);
            }).catch((error)=>{
                setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
                setLoading(false);
            });
        }

    }

    return (
        <div>
            <h2>Sensors{props.location.state && " of \""+props.location.state.deviceName+"\""}</h2>
            {loading ? "Loading...": <br/>}
            {error && <h3 style={{color: "red"}}>{error}</h3>}
            <div className={"container"}>
                <div style={{marginBottom: "10px"}}>{props.match.params.deviceId && "This device has id: "+deviceId}</div>
                {content && (content.length===0 ? <div>Empty</div> : <div style={{marginBottom: "10px"}}><table style={{width:"100%"}}>
                    <thead><tr><td>Id</td><td>Name</td><td>Unit</td><td>Type</td><td>Device name</td><td>Edit</td><td>Remove</td><td>Measurements</td></tr></thead>
                    <tbody>{content.map((sensor,index)=>
                        <tr key={index} className={"row"}>
                            <td>{sensor.id}</td>
                            <td>{sensor.sensorName}</td>
                            <td>{sensor.unit}</td>
                            <td>{sensor.sensorType}</td>
                            <td>{sensor.device.deviceName}</td>
                            <td><div className={"mirror"}><Link to={"/sensor/"+sensor.id} style={{textDecorationLine: "none",padding: "0px"}}>✎</Link></div></td>
                            <td><div><button onClick={()=>handleRemoveSensor(sensor.id)}>X</button></div></td>
                            <td><div><Link to={"/measurements/"+sensor.id} style={{textDecorationLine: "none",padding: "0px"}}>Show</Link></div></td>
                        </tr>)}
                    </tbody>
                </table></div>)}
                <div className={"row"}><h3 style={{marginBottom: "0px"}}>Add sensor</h3></div>
                <div className={"row"}>
                    <div className={"col-15"} style={{width: "11%"}}><label htmlFor={"deviceId"} style={{paddingRight: "0px"}}>To device:</label></div>
                    {props.match.params.deviceId ? <div className={"col-15"} style={{padding: "12px",width: "1%"}}>{deviceId}</div>:<div className={"col-15"}>
                        <select onChange={(e)=>setDeviceId(e.target.value)}>
                            {deviceContent && deviceContent.map((device,index)=><option key={index} value={device.id}>{device.deviceName}</option>)}
                        </select>
                    </div>}
                    <div className={"col-15"} style={{marginLeft: "5px"}}><div className={"submitButton"} style={{float: "left"}}>
                        <Link to={deviceId ? "/sensor-form/"+deviceId : '#'} style={{textDecorationLine: "none", color: "white"}}>Add sensor</Link>
                    </div></div>
                </div>
            </div>
        </div>
    );
}