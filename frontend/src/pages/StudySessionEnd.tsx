import { useNavigate } from 'react-router-dom';

function StudySessionEnd({ plannedMinutes, totalSecondsElapsed, cyclesCompleted, status }) {
    const navigate = useNavigate();

    function handleDashboard() {
        navigate("/dashboard");
    }

    function handleStudyPlan() {
        navigate("/study-plan");
    }

    return (
        <div className="text-center max-w-md mx-auto mt-20">
            <h1 className="text-3xl font-bold text-emerald-50 mb-4">Good job!</h1>
            <p className="text-stone-300 mb-1">
                You planned on studying for {plannedMinutes} minutes and spent a total of {totalSecondsElapsed} minutes studying with {cyclesCompleted} completed cycles.
            </p>
            <p className="text-sm text-stone-500 mb-6">Status: {status}</p>
            <div className="flex gap-3 justify-center">
                <button
                    onClick={handleDashboard}
                    className="bg-emerald-700 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition"
                >
                    Back to dashboard
                </button>
                <button
                    onClick={handleStudyPlan}
                    className="bg-transparent border border-emerald-700 text-emerald-300 px-6 py-2 rounded-lg text-sm font-medium hover:bg-emerald-900/40 transition"
                >
                    Back to study plan
                </button>
            </div>
        </div>
    );
}

export default StudySessionEnd;