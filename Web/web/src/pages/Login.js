import React, {useEffect, useState} from 'react'
import { Grid,Paper, TextField, Button, Typography,Link } from '@mui/material'
import '../styles/Login.css'
import axios from "axios";


function Login(){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [users, setUsers] = useState([]);

    useEffect(() => {
        axios.get('http://3.66.157.143/api/User')
            .then(res => {
                setUsers(res.data)
            })
            .catch(err => {
                console.log(err)
            })
    }, [])


    function found(username, password) {
        for (const user of users) {
            if (user.name === username && user.password === password) {
                console.log(user.name)
                return user
            }
        }
        return null
    }

    const handleLoginButtonClick = () => {
        const user = found(username, password)
        if (user === null) {
            alert("Wrong credentials")
        }
        else
            alert("Hello " + username)
    }

    const handleUsernameChange = event => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = event => {
        setPassword(event.target.value)
    }

    return(
        <div>
            <div>
                <h2>Welcome!</h2>
            </div>
            <div className="Container">
                <Grid spacing={5}>
                    <Paper elevation={10} className="paperStyle">
                        <Grid align='center' >
                            <h2>Sign In</h2>
                        </Grid>
                        <TextField label='Username' placeholder='Enter username' fullWidth required onChange={handleUsernameChange}/>
                        <TextField label='Password' placeholder='Enter password' type='password' fullWidth required onChange={handlePasswordChange}/>

                        <Button className="btnstyle" onClick={handleLoginButtonClick} variant="contained" fullWidth color="primary">Sign in</Button>
                        <Typography className="linkToRegister"> Do you have an account ?
                            <Link href="#" >
                                Sign Up
                            </Link>
                        </Typography>
                    </Paper>
                </Grid>
            </div>
        </div>
    )
}

export default Login