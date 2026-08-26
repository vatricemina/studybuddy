import { useNavigate } from 'react-router-dom';

function StudySessionEnd({ plannedMinutes, totalSecondsElapsed, cyclesCompleted, status }) {
    const navigate = useNavigate();

    function handleButton() {
        navigate("/dashboard");
    }

    return (
        <div className="text-center max-w-md mx-auto">
            <h1 className="text-3xl font-bold text-slate-800 mb-4">Good job! 🎉</h1>
            <p className="text-slate-600 mb-1">
                You planned on studying for {plannedMinutes} minutes and spent a total of {totalSecondsElapsed} minutes studying with {cyclesCompleted} completed cycles.
            </p>
            <p className="text-sm text-slate-400 mb-6">Status: {status}</p>
            <button
                onClick={handleButton}
                className="bg-indigo-400 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition"
            >
                Back to dashboard
            </button>
        </div>
    );
}

export default StudySessionEnd;