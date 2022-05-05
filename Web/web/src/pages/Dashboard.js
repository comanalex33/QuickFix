import React from 'react'
import {useNavigate, useLocation} from "react-router-dom";
import {Button} from "@mui/material"

function Dashboard() {
    const navigate=useNavigate()
    const {state} = useLocation()

    const handleLogOut = () =>{
        navigate('/')
    }
    return(
        <div>
            <h2 >Welcome {state}!</h2>
            <Button variant="contained" onClick={handleLogOut}>Log out</Button>
        </div>
    )
}
export default Dashboard