import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function QuizHistoryPage(){
    const {topicId}=useParams();
    const[quizzes, setQuizzes]=useState([]);

    async function fetchQuizHistory(){
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/quizzes", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        const filtered=response.data.filter(
            (quiz)=>quiz.topicId==Number(topicId)
        );
        setQuizzes(filtered);
    }

    useEffect(()=>{
        fetchQuizHistory();
    }, [topicId]);

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">Quiz History</h1>
                <p className="text-slate-500">Review your past quizzes and scores.</p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                {quizzes.map((quiz) => (
                    <Link
                        key={quiz.id}
                        to={`/quizzes/${quiz.id}`}
                        className="block bg-white/80 p-5 rounded-2xl shadow-sm border border-indigo-100 hover:shadow-md transition"
                    >
                        <p className="text-sm text-slate-500">{formatDate(quiz.generatedAt)}</p>
                        <p className="text-lg font-semibold text-slate-800 mt-1">
                            Score: {quiz.score !== null ? quiz.score : "Not submitted"}
                        </p>
                    </Link>
                ))}
            </div>
        </div>
    );
}

export default QuizHistoryPage;