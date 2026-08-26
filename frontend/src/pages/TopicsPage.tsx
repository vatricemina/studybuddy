import { useState, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import axios from 'axios';

function TopicsPage(){
    const {subjectId} = useParams();
    const [topics, setTopics]=useState([]);
    const [title,setTitle]=useState("");
    const [estimatedHours,setEstimatedHours]=useState("");

    async function fetchTopics(){
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/topics",{
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        const filteredTopics=response.data.filter(
            (topic)=>topic.subjectId==Number(subjectId)
        );
        setTopics(filteredTopics);
    }

    useEffect(()=>{
        fetchTopics();
    }, [subjectId]);

    async function handleAddTopic(){
        const token=localStorage.getItem("token");

        await axios.post("http://localhost:8080/api/topics", {
            title:title,
            estimatedHours:Number(estimatedHours),
            completed:false,
            subjectId:Number(subjectId)
        }, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setTitle("");
        setEstimatedHours("");
        fetchTopics();
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">Topics</h1>
                <p className="text-slate-500">Browse your topics or add a new one to keep studying.</p>
            </div>

            {topics.length === 0 ? (
                <p className="text-slate-400 text-sm mb-8">No topics yet — add your first one below.</p>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 mb-10">
                    {topics.map((topic) => (
                        <div key={topic.id} className="bg-white/80 p-5 rounded-2xl shadow-sm border border-indigo-100 hover:shadow-md transition">
                            <p className="text-lg font-semibold text-slate-800">{topic.title}</p>
                            <p className="text-xs text-slate-400 mt-1">Estimated hours: {topic.estimatedHours}</p>
                            <div className="flex flex-col gap-1 mt-3 text-sm">
                                <Link to={`/topics/${topic.id}/flashcards`} className="text-indigo-500 font-medium hover:underline">Flashcards</Link>
                                <Link to={`/topics/${topic.id}/quiz`} className="text-rose-400 font-medium hover:underline">Quiz</Link>
                                <Link to={`/topics/${topic.id}/chat`} className="text-purple-400 font-medium hover:underline">Chat</Link>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <div className="bg-white/80 p-6 rounded-2xl shadow-sm border border-indigo-100 max-w-md">
                <h2 className="text-lg font-semibold text-slate-700 mb-4">Add new topic</h2>
                <input
                    type="text"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <input
                    type="number"
                    placeholder="Estimated hours"
                    value={estimatedHours}
                    onChange={(e) => setEstimatedHours(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <button
                    onClick={handleAddTopic}
                    className="w-full bg-indigo-400 text-white py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition"
                >
                    Add topic
                </button>
            </div>
        </div>
    );
}

export default TopicsPage;