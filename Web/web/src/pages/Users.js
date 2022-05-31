import React, {useState, useEffect} from 'react';
import '../styles/Users.css'
import axios from 'axios'
import { DropdownButton,Dropdown} from 'react-bootstrap';
import '../styles/Users.css'

const Users = () => {
    const[data,setData]=useState([]);
    const[role,setRole]=useState('')
    const allRoles=["admin","student","handyman"];
    const [filteredRoles,setFilteredRoles]=useState([])
    const [dropdown1,setDropdown1]=useState('')
    const [dropdown2,setDropdown2]=useState('')

    function studentsFilter(){
        axios.get('http://18.196.144.212/api/users/roles/student')
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
        axios.get('http://18.196.144.212/api/users/roles/handyman')
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
        axios.get('http://18.196.144.212/api/users/roles/admin')
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

    function getRoles (username) {
        axios.get(`http://18.196.144.212/api/users/${username}/role`)
            .then(res => {
                setRole(res.data);
                //console.log(res.data)
                filter()
            })
            .catch(err => {
                console.log(err)
            })
    }

    function filter(){
        if(role==="admin"){
            setDropdown1("student");
            setDropdown2("handyman");
        }

        if(role==="student"){
            setDropdown1("admin");
            setDropdown2("handyman");
        }
        if(role==="handyman"){
            setDropdown1("admin");
            setDropdown2("student");
        }
    }



    const usersList = data.map((item)=>
        <div className="card-body"  key={item.id}>
            <h5 className="card-title">{item.userName}</h5>
            <p className="card-text">{item.email}</p>
            <a href="#" className="btn-user">Delete</a>
            <DropdownButton className="btn-user" size="sm" id="dropdown-basic-button" title="Change role" onClick={getRoles(item.userName)}>
                <Dropdown.Item href="#/action-1">{dropdown1}</Dropdown.Item>
                <Dropdown.Item href="#/action-2">{dropdown2}</Dropdown.Item>

            </DropdownButton>
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