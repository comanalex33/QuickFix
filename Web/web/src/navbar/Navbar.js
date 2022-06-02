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
    let isUserLoggedIn=sessionStorage.getItem('token');
    const navigate=useNavigate();

    const handleLogInButton = event =>{
        navigate('/login');
    }

    const handleLogOutButton = event =>{
        navigate('');
        sessionStorage.clear();
    }

    return (
        <>
            <Nav>
                <NavLink to="">
                    <h3>QuickFix</h3>
                </NavLink>
                <NavMenu>
                    <NavLink to="" >
                        Home
                    </NavLink>
                    {(isUserLoggedIn!==null) &&
                    <NavLink to="/buildings" >
                        Buildings
                    </NavLink>}
                    {(isUserLoggedIn!==null) &&
                    <NavLink to="/users" >
                        Users
                    </NavLink>}
                    <NavLink to="/category" >
                        Categories
                    </NavLink>
                </NavMenu>
                {(isUserLoggedIn===null) ?
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