import { Link, useNavigate } from 'react-router-dom';
import { LayoutGrid, Calendar, Brain, Layers, MessageSquare, LogOut } from 'lucide-react';

function Navbar() {
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/login");
    }

    return (
        <nav className="bg-neutral-950 border-b border-emerald-900/40 px-6 py-4">
            <div className="flex items-center gap-2">
                <p className="text-emerald-50 text-lg font-bold mr-6">StudyBuddy</p>

                <Link to="/dashboard" className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-emerald-300 hover:bg-emerald-950/50 transition">
                    <LayoutGrid size={16} />
                    Dashboard
                </Link>

                <Link to="/study-plan" className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-emerald-300 hover:bg-emerald-950/50 transition">
                    <Calendar size={16} />
                    StudyPlan
                </Link>

                <Link to="/quiz/new" className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-emerald-300 hover:bg-emerald-950/50 transition">
                    <Brain size={16} />
                    Quiz
                </Link>

                <Link to="/flashcards/new" className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-emerald-300 hover:bg-emerald-950/50 transition">
                    <Layers size={16} />
                    Flashcards
                </Link>

                <Link to="/chat" className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-emerald-300 hover:bg-emerald-950/50 transition">
                    <MessageSquare size={16} />
                    AI Chat
                </Link>

                <button
                    onClick={handleLogout}
                    className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium text-stone-400 hover:text-red-400 hover:bg-red-950/30 transition ml-auto"
                >
                    <LogOut size={16} />
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;