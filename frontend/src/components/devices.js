import React, {useEffect} from "react";
import {useState} from "react";
import {Link} from "react-router-dom";
import UserService from "../services/user";

export default function Devices() {

    const [content, setContent] = useState(undefined);
    const [error, setError] = useState(undefined);
    const [loading, setLoading] = useState(false);
    const [deviceName, setDeviceName] = useState("");
    const [description, setDescription] = useState("");

    useEffect(()=>{
        setLoading(true);
        UserService.getDataAPI('device',UserService.getUser().id).then(json=>{
            setContent(json);
            setLoading(false);
        }).catch((error)=>{
            setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    },[]);

    const onSubmitHandler = event => {
        event.preventDefault();
        setLoading(true);

        let body = {deviceName: deviceName,description: description,userId: UserService.getUser().id}

        UserService.postDataAPI('device',body,"",true,false).then(json=>{
            setLoading(false);
            setContent(json);
            setDeviceName("");
            setDescription("");
        }).catch((error)=>{
            setError(error.message.length<50 ? error.message:JSON.parse(error.message).message);
            setLoading(false);
        });
    }

    const handleRemoveDevice = id => {
        if(window.confirm("Do you really want remove this device?")){
            setLoading(true);
            UserService.deleteDataAPI('device',undefined,id).then(json=>{
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
            <h2>Devices</h2>
            {loading ? "Loading...": <br/>}
            <div className={"container"}>
                {error ? <h3 style={{color: "red"}}>{error}</h3>:<>
                    {content && (content.length===0 ? <div>Empty</div> :
                    <div style={{marginBottom: "10px"}}><table style={{width:"100%"}}>
                        <thead><tr><td>Id</td><td>Name</td><td>Description</td><td>Show sensors</td><td>Add sensor</td><td>Remove</td></tr></thead><tbody>
                            {content.map((item,index)=>
                            <tr key={index} className={"row"}>
                                <td>{item.id}</td>
                                <td>{item.deviceName}</td>
                                <td>{item.description}</td>
                                <td><div><Link to={{pathname:"/sensors/"+item.id, state: {deviceName: item.deviceName} }} style={{textDecorationLine: "none"}}>Show</Link></div></td>
                                <td><div><Link to={"/sensor-form/"+item.id} style={{textDecorationLine: "none"}}>New</Link></div></td>
                                <td><div><button onClick={()=>handleRemoveDevice(item.id)}>X</button></div></td>
                            </tr>)}
                        </tbody></table>
                    </div>)}
                </>}
                {UserService.getUser().roles.includes("ROLE_ADMIN") && <div><br/>
                    <div className={"transfer-sensors"} style={{backgroundColor: "gray", padding: "10px"}}>
                        <h4>Create new device (ADMIN role only!)</h4>
                        <form onSubmit={onSubmitHandler}>
                            <div className="row">
                                <div className="col-15"><label htmlFor={"name"}>Name: </label></div>
                                <div className="col-85"><input type={"text"} placeholder={"Name"} id={"name"} required={true} value={deviceName} onChange={(e) => setDeviceName(e.target.value)}/></div>
                            </div>
                            <div className="row">
                                <div className="col-15"><label htmlFor={"description"}>Description: </label></div>
                                <div className="col-85"><input type={"text"} placeholder={"Description"} id={"Description"} required={true} value={description} onChange={(e) => setDescription(e.target.value)}/></div>
                            </div>
                            <div className="row"><input type={"submit"} className={"submitButton"} value={"Create new device"}/></div>
                        </form>
                    </div>
                </div>}
            </div>
        </div>
    );
}