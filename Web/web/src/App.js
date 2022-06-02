import './App.css';
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home"
import Navbar from "./navbar/Navbar"
import Category from "./pages/Category"
import Buildings from "./pages/Buildings"
import Users from "./pages/Users"
import {Route, Routes, BrowserRouter} from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
        <Navbar/>
        <Routes>
            <Route path="" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/category" element={<Category />} />
            <Route path="/buildings" element={<Buildings />} />
            <Route path="/users" element={<Users />}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
