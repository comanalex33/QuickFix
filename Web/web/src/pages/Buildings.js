import React, {useState,useEffect} from 'react';
import axios from 'axios'
import {Button,TextField} from '@mui/material'
import Popup from 'reactjs-popup'
import queryString from 'query-string'
import '../styles/Buildings.css'

const Buildings = () => {
    const[data,setData]=useState([]);
    const[name,setName]=useState('')
    const [buttonAdd,setButtonAdd]=useState(false)
    const closeModal = () => setButtonAdd(false);
    useEffect(() => {
        axios.get('http://18.196.144.212/api/buildings')
            .then(res => {
                let l = [];
                for (const user of res.data)
                    l.push(user);
                setData(l);
            })
            .catch(err => {
                console.log(err)
            })
    },[]);

    const buttonPressed=()=>{
        setButtonAdd(true);
    }

    const handleNameChange = event => {
        setName(event.target.value)
    }

    const token=sessionStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': 'Bearer ' + token
        }

    }

    console.log(name)

    function addNewBuilding(){
        let building={
           name: name
        }
        axios.post('http://18.196.144.212/api/buildings', building, config)
            .then(res => {
                console.log(res.data)
                closeModal(true)
            })
            .catch(err => {
                console.log(err)
            })
    }



    const buildingsList = data.map((item)=>
            <div className="col-md-4"  key={item.id}>
                <div className="card mb-4 shadow-sm">
                    <div className="card-body">
                        <h2>{item.name}</h2>
                        <br/>
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
                            <TextField label='Name' placeholder='Enter building name' required onChange={handleNameChange}/>
                        </div>
                        <div className="popup-button">
                            <Button variant="contained" color='info' onClick={addNewBuilding}>Add</Button>
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