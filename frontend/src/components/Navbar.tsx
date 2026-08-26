import { Link, useNavigate } from 'react-router-dom';

function Navbar() {
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/login");
    }

    return (
        <nav className="flex justify-between items-center px-6 py-3 bg-white/70 backdrop-blur-sm border-b border-indigo-100">
            <Link to="/dashboard" className="text-indigo-500 text-lg font-bold tracking-tight">
                StudyBuddy
            </Link>
            <div className="flex items-center gap-6">
                <Link to="/chat" className="text-sm text-slate-500 hover:text-indigo-500 font-medium transition">
                    AI Chat
                </Link>
                <button
                    onClick={handleLogout}
                    className="text-sm text-slate-500 hover:text-indigo-500 font-medium transition"
                >
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;