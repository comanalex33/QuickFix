import React, {useEffect, useState} from 'react';
import axios from 'axios'
import {Button,TextField} from '@mui/material'
import Popup from 'reactjs-popup'
import "../styles/Buildings.css"

const Category = () => {
    const [data, setData] = useState([]);
    const [name, setName] = useState('')
    const [name1, setName1] = useState('')
    const [newName, setNewName] = useState('')
    const [id,setId]=useState(0)
    const [buttonAdd, setButtonAdd] = useState(false)
    const [buttonModify, setButtonModify] = useState(false)
    const closeModal = () => setButtonAdd(false)
    const closeModal2 = () => setButtonModify(false)

    useEffect(() => {
        axios.get('http://18.196.144.212/api/category')
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


    const handleNameChange = event => {
        setName(event.target.value)
    }

    const handleName1Change = event => {
        setName1(event.target.value)
    }
    const handleNewNameChange = event => {
        setNewName(event.target.value)
    }

    const token = sessionStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }

    function addNewCategory() {
        let category = {
            name: name1
        }
        axios.post('http://18.196.144.212/api/category', category, config)
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
                                <Button variant="outlined" /*onClick={()=>saveValue(item.name,item.id)}*/>Modify</Button>
                                <Popup className="building-popup" open={buttonModify} >
                                    <div className="popup-content">
                                        <a className="popup-close"  onClick={closeModal2}>
                                            &times;
                                        </a>
                                        <div className="popup-textfield">
                                            <TextField label='Category name' value={name} required/>
                                        </div>
                                        <div className="popup-textfield">
                                            <TextField label='New category name' placeholder='Enter new category name' required onChange={handleNewNameChange}/>
                                        </div>
                                        <div className="popup-button">
                                            <Button variant="contained" color='info' /*onClick={()=>modifyCategory(id,newName)}*/ >Change name</Button>
                                        </div>

                                    </div>
                                </Popup>
                            </div>
                            <div className="buildings-button">
                                <Button variant="contained" /*onClick={() => handleDeleteButton(item.id)}*/>Delete</Button>
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
                <Button variant="outlined" color='info' onClick={() => setButtonAdd(true)} >Add new category</Button>
                <Popup className="building-popup" open={buttonAdd} closeOnDocumentClick onClose={closeModal}>
                    <div className="popup-content">
                        <a className="popup-close" onClick={closeModal}>
                            &times;
                        </a>
                        <div className="popup-title">
                            <h5>Please enter category name</h5>
                        </div>
                        <div className="popup-textfield">
                            <TextField label='Name' placeholder='Enter category name' required onChange={handleName1Change}/>
                        </div>
                        <div className="popup-button">
                            <Button variant="contained" color='info' onClick={addNewCategory}>Add</Button>
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

export default Category;