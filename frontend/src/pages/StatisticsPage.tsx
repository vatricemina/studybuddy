import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function StatisticsPage() {
    const { subjectId } = useParams();
    const [stats, setStats] = useState(null);
    const [sessions, setSessions] = useState([]);

    async function fetchStats() {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/subjects/${subjectId}/stats`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setStats(response.data);
    }

    async function fetchSessions() {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/study-sessions/subject/${subjectId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setSessions(response.data);
    }

    useEffect(() => { fetchStats(); fetchSessions();}, [subjectId]);

    if (!stats) {
        return <p className="text-slate-500">Loading...</p>;
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-indigo-600 mb-2">
                    {stats.subjectName} — Statistics
                </h1>
                <p className="text-slate-500">See how you're progressing with this subject.</p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-xl mb-10">
                <div className="bg-white/80 p-6 rounded-2xl shadow-sm border border-indigo-100">
                    <p className="text-sm text-slate-500">Total study time</p>
                    <p className="text-3xl font-bold text-indigo-500 mt-1">{stats.totalStudyMinutes} min</p>
                </div>
                <div className="bg-white/80 p-6 rounded-2xl shadow-sm border border-indigo-100">
                    <p className="text-sm text-slate-500">Average quiz score</p>
                    <p className="text-3xl font-bold text-rose-400 mt-1">
                        {stats.averageQuizScore !== null ? stats.averageQuizScore.toFixed(1) : "No quizzes yet"}
                    </p>
                </div>
            </div>

            <div>
                <h2 className="text-lg font-semibold text-slate-700 mb-4">Previous Study Sessions</h2>
                {sessions.length === 0 ? (
                    <p className="text-slate-400 text-sm">No study sessions yet for this subject.</p>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        {sessions.map((session) => (
                            <div key={session.id} className="bg-white/80 p-5 rounded-2xl shadow-sm border border-indigo-100 text-sm">
                                <p className="text-slate-700">Planned study minutes: {session.plannedDurationMinutes}</p>
                                <p className="text-slate-700">Actual study minutes: {session.actualDurationMinutes}</p>
                                <p className="text-slate-500 mt-1">Started at: {session.startedAt}</p>
                                <p className="text-slate-500">Ended at: {session.endedAt}</p>
                                <p className={`mt-2 font-medium ${session.status === "COMPLETED" ? "text-emerald-500" : "text-red-400"}`}>
                                    Status: {session.status}
                                </p>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );

}

export default StatisticsPage;