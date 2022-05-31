import React from 'react';
import '../styles/Home.css'
import {Button} from '@mui/material'
import {useNavigate} from "react-router-dom";

function Home () {
    let isUserLoggedIn=sessionStorage.getItem('token');
    const navigate=useNavigate();
    const handleSignIn = () =>{
        navigate('/login')
    }
    return (
        <div className='home-container'>
            <h2>QUICKFIX ADMINISTRATION</h2>
            <div className='home-btns'>
                {(isUserLoggedIn==null)?
                <Button
                    className='btns'
                    buttonStyle='btn--outline'
                    buttonSize='btn--large'
                    onClick={handleSignIn}
                >
                    GET STARTED
                </Button>:null}
            </div>

            <div className="features">
                        <div className="feature-1">
                            <img src="./images/features-icon-1.png" no-repeat top center/>
                            <h4>Easy to administrate</h4>

                        </div>

                        <div className="feature-2">
                            <img src="./images/features-icon-3.png" no-repeat top center/>
                            <h4>Fastest method</h4>

                        </div>
            </div>
        </div>
    );
};

export default Home;