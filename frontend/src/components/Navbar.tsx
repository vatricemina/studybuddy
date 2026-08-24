import { Link, useNavigate } from 'react-router-dom';


function Navbar() {
    const navigate=useNavigate();

    function handleLogout(){
        localStorage.removeItem("token");
        navigate("/login");
    }

    return(
        <nav style={{
            display: "flex",
            justifyContent: "space-between",
            padding: "15px 20px",
            backgroundColor: "#333",
            color: "white"
        }}>
            <Link to="/dashboard" style={{ color: "white", textDecoration: "none", fontWeight: "bold" }}>StudyBuddy</Link>
            <button onClick={handleLogout}>Logout</button>
        </nav>
    )

}
export default Navbar;