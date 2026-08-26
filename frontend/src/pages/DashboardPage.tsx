import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function DashboardPage(){
    const [subjects, setSubjects] = useState([]);
    const [name, setName] = useState("");
    const [examDate, setExamDate] = useState("");
    const [difficulty, setDifficulty] = useState("");
    const navigate = useNavigate();

    async function fetchSubjects(){
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/subjects", {
            headers: { Authorization: `Bearer ${token}` }
        });
        setSubjects(response.data);
    }

    useEffect(() => { fetchSubjects(); }, []);

    async function handleAddSubject(){
        const today = new Date().toISOString().split("T")[0];
        if (examDate < today) {
            alert("Exam date cannot be in the past");
            return;
        }
        const token = localStorage.getItem("token");
        await axios.post("http://localhost:8080/api/subjects", {
            name: name, examDate: examDate, difficulty: Number(difficulty)
        }, { headers: { Authorization: `Bearer ${token}` } });

        setName(""); setExamDate(""); setDifficulty("");
        fetchSubjects();
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">Welcome back 👋</h1>
                <p className="text-slate-500">Track your subjects, plan your study sessions, and let AI help you stay ahead.</p>
                <p className="text-xs text-indigo-400 italic mt-1">
                    "Success is the sum of small efforts, repeated day in and day out."
                </p>
            </div>

            <div className="mb-10">
                <h2 className="text-lg font-semibold text-slate-700 mb-4">Your Subjects</h2>
                {subjects.length === 0 ? (
                    <p className="text-slate-400 text-sm">No subjects yet — add your first one below to get started.</p>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        {subjects.map((subject) => (
                            <div
                                key={subject.id}
                                onClick={() => navigate(`/subjects/${subject.id}/topics`)}
                                className="bg-white/80 p-5 rounded-2xl shadow-sm border border-indigo-100 hover:shadow-md hover:border-indigo-200 transition cursor-pointer"
                            >
                                <p className="text-lg font-semibold text-slate-800">{subject.name}</p>
                                <p className="text-xs text-slate-400 mt-1">Exam: {subject.examDate}</p>
                                <div className="flex gap-3 mt-3 text-sm">
                                    <span
                                        onClick={(e) => { e.stopPropagation(); navigate(`/subjects/${subject.id}/study-plan`); }}
                                        className="text-rose-400 font-medium hover:underline"
                                    >
                                        Study Plan
                                    </span>
                                    <span
                                        onClick={(e) => { e.stopPropagation(); navigate(`/subjects/${subject.id}/statistics`); }}
                                        className="text-purple-400 font-medium hover:underline"
                                    >
                                        Statistics
                                    </span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <div className="bg-white/80 p-6 rounded-2xl shadow-sm border border-indigo-100 max-w-md">
                <h2 className="text-lg font-semibold text-slate-700 mb-4">Add a new subject</h2>
                <label className="block text-sm text-slate-600 mb-1">Subject name</label>
                <input
                    type="text"
                    placeholder="e.g. Mathematics"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <label className="block text-sm text-slate-600 mb-1">Exam date</label>
                <input
                    type="date"
                    value={examDate}
                    onChange={(e) => setExamDate(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <label className="block text-sm text-slate-600 mb-1">Difficulty</label>
                <select
                    value={difficulty}
                    onChange={(e) => setDifficulty(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                >
                    <option value="">Select difficulty</option>
                    <option value="1">1 - Very easy</option>
                    <option value="2">2 - Easy</option>
                    <option value="3">3 - Medium</option>
                    <option value="4">4 - Hard</option>
                    <option value="5">5 - Very hard</option>
                </select>
                <button
                    onClick={handleAddSubject}
                    className="w-full bg-indigo-400 text-white py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition"
                >
                    Add subject
                </button>
            </div>
        </div>
    );
}

export default DashboardPage;