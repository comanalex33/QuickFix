import './App.css';
import Login from "./pages/Login";
import Register from "./pages/Register";
import {Route, Routes, BrowserRouter, Link} from "react-router-dom";

function App() {
  return (
    <div className="App">
        <div>
            Welcome to QuickFix!
        </div>
        <br/>
        <Link to="/register">Register</Link>
        <div></div>
        <Link to="/login">Login</Link>
    </div>
  );
}

export default App;
