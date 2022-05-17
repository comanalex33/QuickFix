import React, {useState,useEffect} from 'react';
import axios from 'axios'
import {Button,TextField} from '@mui/material'
import Popup from 'reactjs-popup'
import '../styles/Buildings.css'

const Buildings = () => {
    const[data,setData]=useState([]);
    const [buttonAdd,setButtonAdd]=useState(false)
    const closeModal = () => setButtonAdd(false);
    useEffect(() => {
        axios.get('http://3.66.157.143/api/buildings')
            .then(res => {
                let l = [];
                for (const user of res.data)
                    l.push(user);
                setData(l);
            })
            .catch(err => {
                console.log(err)
            })
    }, []);

    const buttonPressed=()=>{
        setButtonAdd(true);
    }

    const buildingsList = data.map((item)=>
            <div className="col-md-4">
                <div className="card mb-4 shadow-sm">
                    <div className="card-body">
                        <h2>{item.name}</h2>
                        <p>{item.id}</p>
                        <div className="d-flex justify-content-between align-items-center">
                            <div className="btn-group">
                                <div className="buildings-button">
                                    <Button variant="outlined">Modify</Button>
                                </div>
                                <div className="buildings-button">
                                    <Button variant="contained">Delete</Button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    );
    return (
        <div>
            <br/>
            <div className="buildings-add-button">
                <Button variant="outlined" color='info' onClick={buttonPressed}>Add new building</Button>
                <Popup className="building-popup" open={buttonAdd} closeOnDocumentClick onClose={closeModal}>
                    <div className="popup-content">
                        <a className="popup-close" onClick={closeModal}>
                            &times;
                        </a>
                        <div className="popup-title">
                            <h5>Please enter building name</h5>
                        </div>
                        <div className="popup-textfield">
                            <TextField label='Name' placeholder='Enter building name' required/>
                        </div>
                        <div className="popup-button">
                            <Button variant="contained" color='info'>Add</Button>
                        </div>

                    </div>
                </Popup>
            </div>
            <div className="album py-5 bg-light buildings">
                <div className="container">
                    <div className="row">
                                {buildingsList}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Buildings;