import React, {useState} from 'react'
import { Grid, Paper, Typography, TextField, Button } from '@mui/material'
import axios from 'axios'
import {useNavigate} from "react-router-dom";
import '../styles/Register.css'


function Register() {
    const [email, setEmail] = useState('')
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [repassword, setRePassword] = useState('')


    const navigate=useNavigate()

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

    const handleSubmit = () => {
        alert('You have submitted the form.')
        let user = {
            username: username,
            email: email,
            password: password
        }

        axios.post(`http://18.196.144.212/api/auth/register`, user)
            .then(res => {
                console.log(res);

            })
            .catch(err => {
                console.log(err)
            });
        navigate('/login')
    }
    return (
        <div>
            <div className="registerTitle">
            </div>
            <div>
                <Grid>
                    <Paper className="paperStyle" elevation={20} >
                        <Grid align='center'>
                            <h2 className="headerStyle">Sign Up</h2>
                            <br/>
                            <Typography variant='caption' gutterBottom>Please fill this form to create an account!</Typography>
                        </Grid>
                        <form className="form" onSubmit={handleSubmit}>
                            <TextField fullWidth label='Name' placeholder="Enter your name" onChange={handleUsernameChange}
                                       />
                            <TextField fullWidth label='Email' placeholder="Enter your email" onChange={handleEmailChange}
                                      />
                            <TextField fullWidth type="password" label='Password' placeholder="Enter your password" onChange={handlePasswordChange}
                                      />
                            <TextField fullWidth type="password" label='Confirm Password' placeholder="Confirm your password" onChange={handleRePasswordChange}
                                     />
                            <div className="registerButton">
                                <Button  type='submit' variant='contained' color='primary'>Sign up</Button>
                            </div>
                        </form>
                    </Paper>
                </Grid>
            </div>
        </div>
    )
}

export default Register;