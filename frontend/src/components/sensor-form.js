import React, {useEffect} from "react";
import {useState} from "react";

import UserService from "../services/user";

export default function SensorForm(props) {

    const [error, setError] = useState(undefined);
    const [loading, setLoading] = useState(false);
    const [sensorId, setSensorId] = useState("");
    const [sensorName, setSensorName] = useState("");
    const [unit, setUnit] = useState("");
    const [sensorType, setSensorType] = useState("");
    const [deviceId, setdeviceId] = useState(0);

    useEffect(()=>{
        if(props.match.params.sensorId) {
            setLoading(true);
            UserService.getDataAPI('sensor',props.match.params.sensorId,true,false).then(json => {
                setSensorId(json.id);
                setSensorName(json.sensorName);
                setUnit(json.unit);
                setSensorType(json.sensorType);
                setdeviceId(json.device.id);
                setLoading(false);
            }).catch((error)=>{
                setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
                setLoading(false);
            });
        }
        if (props.match.params.deviceId){
            setdeviceId(props.match.params.deviceId);
        }
    },[]);

    const onSubmitHandler = event => {
        event.preventDefault();
        setLoading(true);

        let body = {sensorName: sensorName,unit: unit,sensorType: sensorType,deviceId: deviceId}
        if(sensorId){
            body = {id: sensorId,sensorName: sensorName,unit: unit,sensorType: sensorType,deviceId: deviceId}
        }

        UserService.putDataAPI('sensor',body,"",false,false).then(response=>{
            setLoading(false);
            window.history.back();
        }).catch((error)=>{
            setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    }

    return (
        <div>
            <h2>Sensor form</h2>
            {loading ? "Loading...": <br/>}
            {error ? <h3 style={{color: "red"}}>{error}</h3>:
            <div className="container">
                <form onSubmit={onSubmitHandler}>
                    {sensorId &&
                    <div className="row">
                    <div className="col-15"><label htmlFor={"sensorId"}>Id: </label></div>
                    <div className="col-85"><p id={"sensorId"}>{sensorId}</p></div>
                    </div>}
                    <div className="row">
                    <div className="col-15"><label htmlFor={"name"}>Name: </label></div>
                    <div className="col-85"><input type={"text"} placeholder={"Name"} id={"name"} required={true} value={sensorName} onChange={(e) => setSensorName(e.target.value)}/></div>
                    </div>
                    <div className="row">
                    <div className="col-15"><label htmlFor={"unit"}>Unit: </label></div>
                    <div className="col-85"><input type={"text"} placeholder={"Unit"} id={"unit"} required={true} value={unit} onChange={(e) => setUnit(e.target.value)}/></div>
                    </div>
                    <div className="row">
                    <div className="col-15"><label htmlFor={"sensorType"}>Sensor type: </label></div>
                    <div className="col-85"><input type={"text"} placeholder={"Sensor type"} id={"sensorType"} required={true} value={sensorType} onChange={(e) => setSensorType(e.target.value)}/></div>
                    </div>
                    <div className="row">
                    <div className="col-15"><label htmlFor={"device"}>Device Id: </label></div>
                    <div className="col-85"><p id={"device"}>{deviceId}</p></div>
                    </div>
                    <div className="row"><input type={"submit"} className={"submitButton"} value={sensorId ? "Edit":"Add"}/></div>
                </form>
            </div>}
        </div>
    );
}