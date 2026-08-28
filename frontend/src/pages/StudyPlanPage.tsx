import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function StudyPlanPage() {
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");


    async function fetchExistingPlans(){
        const token=localStorage.getItem("token");
        const subjectsResponse=await axios.get(`${import.meta.env.VITE_API_URL}/api/subjects`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        const subjects = subjectsResponse.data;

        let allEntries=[];//dohvati sve stavke od svih predmeta
        for (const subject of subjects) {
            try {
                const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/study-plan/${subject.id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                allEntries = [...allEntries, ...response.data];
            } catch (error) {
                console.log(`No plan for subject ${subject.id}`);
            }
        }
        setEntries(allEntries);
    }

    useEffect(() => {
        fetchExistingPlans();
    }, []);

    async function handleGenerate(){
        setError("");
        setLoading(true);
        try{
            const token=localStorage.getItem("token");
            const subjectsResponse=await axios.get(`${import.meta.env.VITE_API_URL}/api/subjects`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const subjects = subjectsResponse.data;

            if(subjects.length===0){
                setError("Add at least one subject first.");
                setLoading(false);
                return;
            }
            let allEntries=[];
            for(const subject of subjects){ //generise plan za svaki predmet i sve stavi u jednu listu allEntries
                const response=await axios.post(`${import.meta.env.VITE_API_URL}/api/study-plan/generate`, {
                    subjectId: subject.id
                }, { headers: { Authorization: `Bearer ${token}` } });
                allEntries=[...allEntries, ...response.data];
            }
            setEntries(allEntries);
        }catch(e){
            setError("Some subjects have no topics - add topics before generating a plan");
        }finally{
            setLoading(false);
        }

    }

    const grouped=entries.reduce((acc,entry)=>{ //grupisi sve stavke plana po datumu
        if(!acc[entry.plannedDate]) acc[entry.plannedDate] = [];
        acc[entry.plannedDate].push(entry);
        return acc;
    }, {});

    const sortedDates=Object.keys(grouped).sort();

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">Study Plan</h1>
                <p className="text-stone-400">Study smarter. Let AI build a study schedule tailored to your exam date.</p>
            </div>

            <button
                onClick={handleGenerate}
                disabled={loading}
                className="bg-emerald-700 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition disabled:opacity-50 mb-8"
            >
                {loading ? "Generating..." : "Generate new plan"}
            </button>

            {error && <p className="text-sm text-red-400 mb-6">{error}</p>}

            <div className="space-y-4">
                {sortedDates.map((date) => (
                    <div key={date} className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-5">
                        <p className="text-emerald-300 font-semibold mb-3">{date}</p>
                        <div className="space-y-3">
                            {grouped[date].map((entry) => (
                                <div key={entry.id} className="border-t border-emerald-900/30 pt-3 first:border-0 first:pt-0">
                                    <p className="text-emerald-50 font-medium">
                                        {entry.topicTitle} — {entry.subjectName} ({entry.plannedHours}h)
                                    </p>
                                    <p className="text-sm text-stone-400 mb-2">{entry.focus}</p>
                                    <Link
                                        to={`/topics/${entry.topicId}/study-session`}
                                        className="text-sm text-emerald-400 font-medium hover:underline"
                                    >
                                        Start Session →
                                    </Link>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );

}
export default StudyPlanPage;