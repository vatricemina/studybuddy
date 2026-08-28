import { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Trash2, BookOpen, ClipboardList, BarChart3 } from 'lucide-react';

function daysUntil(dateString){
    const diff = new Date(dateString) - new Date();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
}

function difficultyLabel(value){
    const labels = { "1": "Very easy", "2": "Easy", "3": "Medium", "4": "Hard", "5": "Very hard" };
    return labels[String(value)] || "";
}

function DashboardPage(){
    const [subjects, setSubjects] = useState([]);
    const [name, setName] = useState("");
    const [examDate, setExamDate] = useState("");
    const [difficulty, setDifficulty] = useState("");
    const [firstName, setFirstName] = useState("");
    const [error, setError] = useState("");

    async function fetchSubjects(){
        const token = localStorage.getItem("token");
        const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/subjects`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setSubjects(response.data);
    }

    async function fetchUser(){
        const token = localStorage.getItem("token");
        const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/users/me`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setFirstName(response.data.firstName);
    }

    useEffect(() => { fetchSubjects(); fetchUser(); }, []);

    async function handleAddSubject(){
        setError("");
        if (name.trim() === "" || examDate === "" || difficulty === "") {
            setError("Please fill in all fields");
            return;
        }

        const today = new Date().toISOString().split("T")[0];
        if (examDate < today) {
            setError("Exam date cannot be in the past");
            return;
        }

        const token = localStorage.getItem("token");
        await axios.post(`${import.meta.env.VITE_API_URL}/api/subjects`, {
            name: name, examDate: examDate, difficulty: Number(difficulty)
        }, { headers: { Authorization: `Bearer ${token}` } });

        setName(""); setExamDate(""); setDifficulty("");
        fetchSubjects();
    }

    async function handleDeleteSubject(subjectId){
        const token = localStorage.getItem("token");
        await axios.delete(`${import.meta.env.VITE_API_URL}/api/subjects/${subjectId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        fetchSubjects();
    }

    const nearestExamDays = subjects.length > 0
        ? Math.min(...subjects.map(s => daysUntil(s.examDate)))
        : null;

    return (
        <div>
            <div className="mb-10">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">
                    Welcome back, {firstName}!
                </h1>
                {subjects.length > 0 ? (
                    <p className="text-stone-400">
                        {subjects.length} courses in progress. First exam in {nearestExamDays} days.
                    </p>
                ) : (
                    <p className="text-stone-400">
                        Track your subjects, plan your study sessions and let AI help you stay ahead.
                    </p>
                )}
            </div>

            <h2 className="text-lg font-semibold text-emerald-50 mb-4">Your courses</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-10">
                {subjects.map((subject) => (
                    <div
                        key={subject.id}
                        className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-5 hover:border-emerald-700 transition"
                    >
                        <div className="flex justify-between items-start mb-2">
                            <div className="flex items-center gap-2">
                                <BookOpen size={18} className="text-emerald-400" />
                                <p className="text-lg font-semibold text-emerald-50">{subject.name}</p>
                            </div>
                            <button
                                onClick={() => handleDeleteSubject(subject.id)}
                                className="text-stone-500 hover:text-red-400 transition"
                            >
                                <Trash2 size={18} />
                            </button>
                        </div>
                        <p className="text-sm text-stone-400">Exam: {subject.examDate}</p>
                        <p className="text-sm text-stone-400">Days until exam: {daysUntil(subject.examDate)}</p>
                        <p className="text-sm text-stone-400 mb-4">Difficulty: <span className="text-emerald-300 font-medium">{difficultyLabel(subject.difficulty)}</span></p>

                        <div className="flex gap-2">
                            <Link
                                to={`/subjects/${subject.id}/topics`}
                                className="flex items-center gap-1 bg-emerald-900/40 text-emerald-300 text-xs font-medium px-3 py-1.5 rounded-full hover:bg-emerald-900/70 transition"
                            >
                                <ClipboardList size={14} /> Topics
                            </Link>
                            <Link
                                to={`/subjects/${subject.id}/statistics`}
                                className="flex items-center gap-1 bg-emerald-900/40 text-emerald-300 text-xs font-medium px-3 py-1.5 rounded-full hover:bg-emerald-900/70 transition"
                            >
                                <BarChart3 size={14} /> Statistics
                            </Link>
                        </div>
                    </div>
                ))}
            </div>

            <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-6 max-w-md">
                <h2 className="text-lg font-semibold text-emerald-50 mb-4">Add new course</h2>
                <input
                    type="text"
                    placeholder="Subject name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <input
                    type="date"
                    value={examDate}
                    onChange={(e) => setExamDate(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <select
                    value={difficulty}
                    onChange={(e) => setDifficulty(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                >
                    <option value="">Select difficulty</option>
                    <option value="1">1 - Very easy</option>
                    <option value="2">2 - Easy</option>
                    <option value="3">3 - Medium</option>
                    <option value="4">4 - Hard</option>
                    <option value="5">5 - Very hard</option>
                </select>

                {error && <p className="text-sm text-red-400 mb-3">{error}</p>}

                <button
                    onClick={handleAddSubject}
                    className="w-full bg-emerald-700 text-white py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition"
                >
                    Add subject
                </button>
            </div>
        </div>
    );
}

export default DashboardPage;