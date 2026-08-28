import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function GenerateFlashcardsPage(){
    const [subjects, setSubjects] = useState([]);
    const [allTopics, setAllTopics] = useState([]);
    const [selectedSubject, setSelectedSubject] = useState("");
    const [selectedTopic, setSelectedTopic] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    async function fetchData(){
        const token = localStorage.getItem("token");
        const subjectsResponse = await axios.get(`${import.meta.env.VITE_API_URL}/api/subjects`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        const topicsResponse = await axios.get(`${import.meta.env.VITE_API_URL}/api/topics`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setSubjects(subjectsResponse.data);
        setAllTopics(topicsResponse.data);
    }

    useEffect(() => {
        fetchData();
    }, []);

    const filteredTopics=allTopics.filter((t)=>t.subjectId===Number(selectedSubject));

    async function handleGenerate(){
        setError("");
        if(selectedSubject==="" || selectedTopic===""){
            setError("Please select a course and a topic");
            return;
        }
        setLoading(true);
        try{
            const token = localStorage.getItem("token");
            await axios.post(`${import.meta.env.VITE_API_URL}/api/flashcards/generate`


                , {
                topicId: Number(selectedTopic)
            }, { headers: { Authorization: `Bearer ${token}` } });

            navigate(`/topics/${selectedTopic}/flashcards`);
        }catch(err){
            setError("Something went wrong. Please try again.");
        }finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">New Flashcards</h1>
                <p className="text-stone-400">Fill in the form and let AI test your knowledge.</p>
            </div>

            <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-6 max-w-md">
                <label className="block text-sm text-stone-400 mb-1">Course</label>
                <select
                    value={selectedSubject}
                    onChange={(e) => { setSelectedSubject(e.target.value); setSelectedTopic(""); }}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-4 text-sm text-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                >
                    <option value="">Select course</option>
                    {subjects.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
                </select>

                <label className="block text-sm text-stone-400 mb-1">Topic</label>
                <select
                    value={selectedTopic}
                    onChange={(e) => setSelectedTopic(e.target.value)}
                    disabled={!selectedSubject}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-4 text-sm text-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-700 disabled:opacity-40"
                >
                    <option value="">Select topic</option>
                    {filteredTopics.map((t) => <option key={t.id} value={t.id}>{t.title}</option>)}
                </select>

                {error && <p className="text-sm text-red-400 mb-3">{error}</p>}

                <button
                    onClick={handleGenerate}
                    disabled={loading}
                    className="w-full bg-emerald-700 text-white py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition disabled:opacity-50"
                >
                    {loading ? "Generating..." : "Generate Flashcards"}
                </button>
            </div>
        </div>
    );
}
export default GenerateFlashcardsPage;