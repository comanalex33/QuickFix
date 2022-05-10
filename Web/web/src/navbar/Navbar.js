import React from 'react';
import {
    Nav,
    NavLink,
    NavMenu,
    NavBtn,
    NavBtnLink
} from './NavbarElements';
import {useNavigate} from "react-router-dom";

const Navbar = () => {

    let isUserLoggedIn=localStorage.getItem('token');
    const navigate=useNavigate();

    const handleLogInButton = event =>{
        navigate('/login');
    }

    const handleLogOutButton = event =>{
        navigate('');
        localStorage.setItem('token',null);
    }

    return (
        <>
            <Nav>
                <NavLink to="">
                    <h1>QuickFix</h1>
                </NavLink>
                <NavMenu>
                    <NavLink to="/dashboard" >
                        Home
                    </NavLink>
                    <NavLink to="/buildings" >
                        Buildings
                    </NavLink>
                    <NavLink to="/users" >
                        Users
                    </NavLink>
                    <NavLink to="/contact" >
                        Contact
                    </NavLink>
                </NavMenu>
                {(isUserLoggedIn==="null") ?
                    <NavBtn>
                        <NavBtnLink onClick={handleLogInButton}>Sign In</NavBtnLink>
                    </NavBtn> :
                    <NavBtn>
                        <NavBtnLink onClick={handleLogOutButton}>Sign Out</NavBtnLink>
                    </NavBtn>
                }
            </Nav>
        </>
    );
};

export default Navbar;