import React, {useState,useEffect} from 'react';
import axios from 'axios'
import {Button,TextField} from '@mui/material'
import Popup from 'reactjs-popup'
import '../styles/Buildings.css'

const Buildings = () => {
    const[data,setData]=useState([]);
    const[name,setName]=useState('')
    const [buttonAdd,setButtonAdd]=useState(false)
    const [buttonModify, setButtonModify] = useState(false)
    const [newName, setNewName] = useState('')
    const [id,setId]=useState(0)
    const [error,setError]=useState('')
    const closeModal = () => setButtonAdd(false)
    const closeModal2 = () => {
        setButtonModify(false)
        setNewName(null)

    }

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
    });

    const buttonPressed=()=>{
        setButtonAdd(true);
    }

    const handleNameChange = event => {
        setName(event.target.value)
    }

    const handleNewNameChange = event => {
        setNewName(event.target.value)
        setError('')
    }

    const token=sessionStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': 'Bearer ' + token
        }

    }

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

    const handleDeleteButton = id => {
        if(window.confirm("Are you sure you want to delete?"))
        {
            console.log(id)
            axios.delete(`http://18.196.144.212/api/buildings/${id}`,config)
                .then((response) => {
                    console.log(response);
                })
        }
    }

    const modifyBuilding= (id, name) => {

        axios.put(`http://18.196.144.212/api/buildings/${id}/name/${name}`,null,config)
            .then(res => {
                console.log(res.data)
                closeModal2(true)
            })
            .catch(err => {
                console.log(err)
                if(err.response.status===400){
                    setError(err.response.data)
                    //console.log(error)
                }
            })
    }

    const saveValue = (name,id) => {
        setButtonModify(true);
        setName(name);
        setId(id)
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
                                    <Button variant="outlined" onClick={()=>saveValue(item.name,item.id)}>Modify</Button>
                                    <Popup className="building-popup" open={buttonModify} >
                                        <div className="popup-content">
                                            <a className="popup-close"  onClick={closeModal2}>
                                                &times;
                                            </a>
                                            <div className="popup-textfield">
                                                <TextField label='Building name' value={name} required/>
                                            </div>
                                            <div className="popup-textfield">
                                                <TextField label='New building name' placeholder='Enter new building name' required
                                                           onChange={handleNewNameChange} error={name === newName}
                                                           helperText={newName === name ? 'The new name is identical!' : ' '}/>
                                                <h6>{error}</h6>
                                            </div>
                                            <div className="popup-button">
                                                <Button variant="contained" color='info' onClick={()=>modifyBuilding(id,newName)}>Change name</Button>
                                            </div>

                                        </div>
                                    </Popup>
                                </div>
                                <div className="buildings-button">
                                    <Button variant="contained" onClick={() => handleDeleteButton(item.id)}>Delete</Button>
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