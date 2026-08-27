import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Clock, TrendingUp } from 'lucide-react';

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

    function formatDate(dateString) {
        if (!dateString) return "—";
        const date = new Date(dateString);
        return date.toLocaleString();
    }

    useEffect(() => {
        fetchStats();
        fetchSessions();
    }, [subjectId]);

    if (!stats) {
        return <p className="text-stone-400">Loading...</p>;
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">
                    {stats.subjectName} — Statistics
                </h1>
                <p className="text-stone-400">See how you're progressing with this subject.</p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-xl mb-10">
                <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-6">
                    <div className="flex items-center gap-2 mb-1">
                        <Clock size={16} className="text-emerald-400" />
                        <p className="text-sm text-stone-400">Total study time</p>
                    </div>
                    <p className="text-3xl font-bold text-emerald-300 mt-1">{stats.totalStudyMinutes} min</p>
                </div>
                <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-6">
                    <div className="flex items-center gap-2 mb-1">
                        <TrendingUp size={16} className="text-emerald-400" />
                        <p className="text-sm text-stone-400">Average quiz score</p>
                    </div>
                    <p className="text-3xl font-bold text-emerald-300 mt-1">
                        {stats.averageQuizScore !== null ? stats.averageQuizScore.toFixed(1) : "No quizzes yet"}
                    </p>
                </div>
            </div>

            <div>
                <h2 className="text-lg font-semibold text-emerald-50 mb-4">Previous Study Sessions</h2>
                {sessions.length === 0 ? (
                    <p className="text-stone-500 text-sm">No study sessions yet for this subject.</p>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        {sessions.map((session) => (
                            <div key={session.id} className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-5 text-sm">
                                <p className="text-emerald-50">Planned study minutes: {session.plannedDurationMinutes}</p>
                                <p className="text-emerald-50">
                                    Actual study minutes: {session.actualDurationMinutes !== null ? session.actualDurationMinutes : "In progress"}
                                </p>
                                <p className="text-stone-400 mt-1">Started at: {formatDate(session.startedAt)}</p>
                                <p className="text-stone-400">Ended at: {formatDate(session.endedAt)}</p>
                                <p className={`mt-2 font-medium ${session.status === "COMPLETED" ? "text-emerald-400" : "text-rose-400"}`}>
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