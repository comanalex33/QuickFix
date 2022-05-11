import './App.css';
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard"
import Home from "./pages/Home"
import Navbar from "./navbar/Navbar"
import Contact from "./pages/Contact"
import Buildings from "./pages/Buildings"
import {Route, Routes, BrowserRouter, Link} from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
        <Navbar/>
        <Routes>
            <Route path="" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/buildings" element={<Buildings />} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;
