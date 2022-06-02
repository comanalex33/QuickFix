import React, {useState, useEffect} from 'react';
import '../styles/Users.css'
import axios from 'axios'
import { DropdownButton,Dropdown} from 'react-bootstrap';
import '../styles/Users.css'

function Users () {
    const[data,setData]=useState([]);
    const[role,setRole]=useState('')
    const [dropdown1,setDropdown1]=useState('')
    const [dropdown2,setDropdown2]=useState('')
    const [dropdown3,setDropdown3]=useState('')

    useEffect(() => {
        //studentsFilter()
    }, []);


    const studentsFilter =()=>{
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

    const handyMansFilter=()=>{
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

    const adminsFilter =()=>{
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
            axios.get(`http://18.196.144.212/api/users/${username}/role`,config)
                .then(res => {
                    setRole(res.data);
                    //console.log(res.data)
                    filter()
                })
                .catch(err => {
                    console.log(err)

                })




    }
    const noRoleFilter= ()=>{
        axios.get('http://18.196.144.212/api/users/roles/none')
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

    function filter(){
        if(role==="admin"){
            setDropdown1("student");
            setDropdown2("handyman");
            setDropdown3("")
            return
        }

        if(role==="student"){
            setDropdown1("admin");
            setDropdown2("handyman");
            setDropdown3("")
            return
        }
        if(role==="handyman"){
            setDropdown1("admin");
            setDropdown2("student");
            setDropdown3("")
            return
        }
        else{
            setDropdown1("admin");
            setDropdown2("student");
            setDropdown3("handyman");
            return
        }

    }

    const token=sessionStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }

    const handleDeleteRole =(e,username)=>{
        if(role!=='none') {
            axios.delete(`http://18.196.144.212/api/users/${username}/removeRole`, config)
                .then(res => {
                    console.log(res.data)
                    handelChangeRole(e, username)

                })
                .catch(err => {
                    console.log(err)
                })
        }
       else{
            handelChangeRole(e, username)
        }

    }

    const handelChangeRole=(e,username)=>{
        if(e==='admin'){
            axios.post(`http://18.196.144.212/api/users/${username}/makeadmin`, null, config)
                .then(res => {
                    console.log(res.data)
                })
                .catch(err => {
                    console.log(err)
                })
        }
        else {
            axios.post(`http://18.196.144.212/api/users/${username}/roles/${e}`, null, config)
                .then(res => {
                    console.log(res.data)
                })
                .catch(err => {
                    console.log(err)
                })
        }
        if(role==='admin') adminsFilter();
        if(role==='student') studentsFilter();
        if(role==='handyman') handyMansFilter();
        if(role==='none') noRoleFilter()
    }

   const deleteAccount =username=>{
        axios.delete(`http://18.196.144.212/api/users/${username}`, config)
            .then(res => {
                console.log(res.data)
            })
            .catch(err => {
                console.log(err)
            })
    }


    const usersList = data.map((item)=>
        <div className="card-body"  key={item.id}>
            <h5 className="card-title" >{item.userName}</h5>
            <p className="card-text">{item.email}</p>
            <button className="btn-user" onClick={()=>deleteAccount(item.userName)}>Delete</button>
            <DropdownButton className="btn-user" size="sm" id="dropdown-basic-button" title="Change role" onClick={getRoles(item.userName)}
                            onSelect={(event) => handleDeleteRole(event,item.userName)}>
                <Dropdown.Item eventKey={dropdown1} >{dropdown1}
                </Dropdown.Item>
                <Dropdown.Item eventKey={dropdown2} >{dropdown2}
                </Dropdown.Item>
                <Dropdown.Item eventKey={dropdown3} >{dropdown3}
                </Dropdown.Item>

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
                        <button type="button" className="btn btn-category" onClick={noRoleFilter}>Users without role</button>
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