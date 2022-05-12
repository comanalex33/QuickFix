import React, {useState, useEffect} from 'react';
import '../styles/Users.css'
import axios from 'axios'
import '../styles/Users.css'

const Users = () => {
    const[data,setData]=useState([]);

    function studentsFilter(){
        axios.get('http://3.66.157.143/api/users/roles/student')
            .then(res => {
                let l = [];
                for (const user of res.data)
                    l.push(user);
                setData(l);
            })
            .catch(err => {
                console.log(err)
            })
    }

    function handyMansFilter(){
        axios.get('http://3.66.157.143/api/users/roles/handyman')
            .then(res => {
                let l = [];
                for (const user of res.data)
                    l.push(user);
                setData(l);
            })
            .catch(err => {
                console.log(err)
            })
    }

    function adminsFilter(){
        axios.get('http://3.66.157.143/api/users/roles/admin')
            .then(res => {
                let l = [];
                for (const user of res.data)
                    l.push(user);
                setData(l);
            })
            .catch(err => {
                console.log(err)
            })
    }

    const usersList = data.map((item)=>
        <div className="card-body">
            <h5 className="card-title">{item.userName}</h5>
            <p className="card-text">{item.email}</p>
            <a href="#" className="btn-user">Delete</a>
            <a href="#" className="btn-user">Change role</a>
        </div>
    );
    return (
        <div>
            <div className="users-container">
                <div className="first-row">
                    <div className="first-column" role="group" aria-label="Basic example">
                        <button type="button" className="btn btn-category" onClick={studentsFilter}>Students</button>
                        <button type="button" className="btn btn-category" onClick={handyMansFilter}>Handymans</button>
                        <button type="button" className="btn btn-category" onClick={adminsFilter}>Admins</button>
                    </div>
                    <div className="second-column">
                        <div className="rows">
                                <div className="card" >
                                {usersList}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Users;