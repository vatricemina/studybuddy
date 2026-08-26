import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function StatisticsPage() {
    const { subjectId } = useParams();
    const [stats, setStats] = useState(null);

    async function fetchStats() {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/subjects/${subjectId}/stats`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setStats(response.data);
    }

    useEffect(() => { fetchStats(); }, [subjectId]);

    if (!stats) {
        return <p className="text-slate-500">Loading...</p>;
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">{stats.subjectName} — Statistics</h1>
                <p className="text-slate-500">See how you're progressing with this subject.</p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-xl">
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
        </div>
    );
}

export default StatisticsPage;