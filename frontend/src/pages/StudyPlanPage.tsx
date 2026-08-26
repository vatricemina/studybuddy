import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function StudyPlanPage() {
    const {subjectId}=useParams();
    const [plan, setPlan]=useState([]);
    const [loading, setLoading]=useState(false);
    const [error, setError] = useState("");

    async function fetchPlan(){
        const token=localStorage.getItem("token");
        const response=await axios.get(`http://localhost:8080/api/study-plan/${subjectId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        setPlan(response.data);
    }

    useEffect(() => {
        fetchPlan();
    }, [subjectId]);

    async function handleGenerate(){
        setLoading(true);
        try{
            const token=localStorage.getItem("token");
            const response=await axios.post("http://localhost:8080/api/study-plan/generate", {
                subjectId:Number(subjectId)
            },{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setPlan(response.data);
        }catch(error){
            if (error.response && error.response.data && error.response.data.message) {
                setError(error.response.data.message);
            } else {
                setError("Something went wrong. Please try again.");
            }
        }finally{
            setLoading(false);
        }

    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-indigo-600 mb-2">Study Plan</h1>
                <p className="text-slate-500">Let AI build a study schedule tailored to your exam date.</p>
            </div>

            <button
                onClick={handleGenerate}
                disabled={loading}
                className="bg-indigo-400 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition disabled:opacity-50 mb-6"
            >
                {loading ? "Generating..." : "Generate New Plan"}
            </button>
            {error && (
                <p className="text-sm text-red-500 mb-4">{error}</p>
            )}

            <div className="space-y-4">
                {plan.map((entry) => (
                    <div key={entry.id} className="bg-white/80 p-5 rounded-2xl shadow-sm border border-indigo-100">
                        <p className="font-semibold text-slate-800">
                            {entry.plannedDate} — {entry.topicTitle} ({entry.plannedHours}h)
                        </p>
                        <p className="text-sm text-slate-500 mt-1">{entry.focus}</p>
                        <Link
                            to={`/topics/${entry.topicId}/study-session`}
                            className="inline-block mt-3 text-sm text-rose-400 font-medium hover:underline"
                        >
                            Start Session →
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );

}
export default StudyPlanPage;