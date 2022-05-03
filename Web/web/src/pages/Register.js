import React, {useState} from 'react'
import { Grid, Paper, Typography, TextField, Button } from '@mui/material'
import axios from "axios";
import {useNavigate} from "react-router-dom";
import '../styles/Register.css'


function Register() {
    const [email, setEmail] = useState('')
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [repassword, setRePassword] = useState('')

    const navigate=useNavigate()
    function handleSignUp() {
        if(username === ''){
            alert('Username field is empty!')
            return
        }

        if(email === ''){
            alert('Email field is empty!')
            return
        }

        if(password === ''){
            alert('Password field is empty!')
            return
        }

        if(password !== repassword) {
            alert('Password missmatch!')
            return
        }

        let user = {
            name: username,
            email: email,
            password: password,
            role: "admin",
        };

        axios.post('http://3.66.157.143/api/User', user)
            .then(res => {
                console.log(res);
                navigate('/login')
            })
            .catch(err => {
                console.log(err)
            });
    }

    const handleEmailChange = event => {
        setEmail(event.target.value)
    }

    const handleUsernameChange = event => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = event => {
        setPassword(event.target.value)
    }

    const handleRePasswordChange = event => {
        setRePassword(event.target.value)
    }
    return (
        <div>
            <div className="registerTitle">
                <h2>Register Page</h2>
            </div>
            <div>
                <Grid>
                    <Paper className="paperStyle" elevation={20} >
                        <Grid align='center'>
                            <h2 className="headerStyle">Sign Up</h2>
                            <br/>
                            <Typography variant='caption' gutterBottom>Please fill this form to create an account!</Typography>
                        </Grid>
                        <form className="form">
                            <TextField fullWidth label='Name' placeholder="Enter your name" onChange={handleUsernameChange}/>
                            <TextField fullWidth label='Email' placeholder="Enter your email" onChange={handleEmailChange} />
                            <TextField fullWidth label='Password' placeholder="Enter your password" onChange={handlePasswordChange}/>
                            <TextField fullWidth label='Confirm Password' placeholder="Confirm your password" onChange={handleRePasswordChange}/>
                            <div className="registerButton">
                                <Button  type='submit' variant='contained' color='primary' onClick={handleSignUp} >Sign up</Button>
                            </div>
                        </form>
                    </Paper>
                </Grid>
            </div>
        </div>
    )
}

export default Register;