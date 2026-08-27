import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { Trash2, Layers, Brain } from 'lucide-react';

function TopicsPage(){
    const { subjectId } = useParams();
    const [topics, setTopics] = useState([]);
    const [title, setTitle] = useState("");
    const [estimatedHours, setEstimatedHours] = useState("");
    const [error, setError] = useState("");

    async function fetchTopics(){
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/topics", {
            headers: { Authorization: `Bearer ${token}` }
        });
        const filtered = response.data.filter((topic) => topic.subjectId === Number(subjectId));
        setTopics(filtered);
    }

    useEffect(() => { fetchTopics(); }, [subjectId]);

    async function handleAddTopic(){
        setError("");
        if (title.trim() === "" || estimatedHours === "") {
            setError("Please fill in all fields");
            return;
        }
        if (Number(estimatedHours) <= 0) {
            setError("Estimated hours must be greater than 0");
            return;
        }

        const token = localStorage.getItem("token");
        await axios.post("http://localhost:8080/api/topics", {
            title: title, estimatedHours: Number(estimatedHours), completed: false, subjectId: Number(subjectId)
        }, { headers: { Authorization: `Bearer ${token}` } });

        setTitle(""); setEstimatedHours("");
        fetchTopics();
    }

    async function handleDeleteTopic(topicId) {
        const token = localStorage.getItem("token");
        await axios.delete(`http://localhost:8080/api/topics/${topicId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        fetchTopics();
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">Topics</h1>
                <p className="text-stone-400">Browse your topics or add a new one to keep studying.</p>
            </div>

            {topics.length === 0 ? (
                <p className="text-stone-500 text-sm mb-8">No topics yet — add your first one below.</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-10">
                    {topics.map((topic) => (
                        <div key={topic.id} className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-5">
                            <div className="flex justify-between items-start mb-2">
                                <p className="text-lg font-semibold text-emerald-50">{topic.title}</p>
                                <button onClick={() => handleDeleteTopic(topic.id)} className="text-stone-500 hover:text-red-400 transition">
                                    <Trash2 size={18} />
                                </button>
                            </div>
                            <p className="text-sm text-stone-400 mb-3">Estimated hours: {topic.estimatedHours}</p>
                            <div className="flex gap-2">
                                <Link to={`/topics/${topic.id}/flashcards`} className="flex items-center gap-1 bg-emerald-900/40 text-emerald-300 text-xs font-medium px-3 py-1.5 rounded-full hover:bg-emerald-900/70 transition">
                                    <Layers size={14} /> Flashcards
                                </Link>
                                <Link to={`/topics/${topic.id}/quiz`} className="flex items-center gap-1 bg-emerald-900/40 text-emerald-300 text-xs font-medium px-3 py-1.5 rounded-full hover:bg-emerald-900/70 transition">
                                    <Brain size={14} /> Quiz
                                </Link>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-6 max-w-md">
                <h2 className="text-lg font-semibold text-emerald-50 mb-4">Add new topic</h2>
                <input
                    type="text"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <input
                    type="number"
                    placeholder="Estimated hours"
                    value={estimatedHours}
                    onChange={(e) => setEstimatedHours(e.target.value)}
                    min="1"
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                {error && <p className="text-sm text-red-400 mb-3">{error}</p>}
                <button onClick={handleAddTopic} className="w-full bg-emerald-700 text-white py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition">
                    Add topic
                </button>
            </div>
        </div>
    );
}

export default TopicsPage;