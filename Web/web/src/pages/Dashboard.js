import React from 'react'
import {useNavigate, useLocation} from "react-router-dom";
import {Button} from "@mui/material"

function Dashboard() {
    const {state} = useLocation()

    return(
        <div>
            <h2 >Welcome {state}!</h2>
        </div>
    )
}
export default Dashboard