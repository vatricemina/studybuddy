import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

function QuizHistoryPage() {
    const { topicId } = useParams();
    const [quizzes, setQuizzes] = useState([]);

    async function fetchQuizHistory() {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/quizzes", {
            headers: { Authorization: `Bearer ${token}` }
        });

        setQuizzes(response.data);
    }

    useEffect(() => {
        fetchQuizHistory();
    }, [topicId]);

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">Quiz History</h1>
                <p className="text-stone-400">Review your past quizzes and scores.</p>
            </div>

            {quizzes.length === 0 ? (
                <p className="text-stone-500 text-sm">No quizzes generated yet for this topic.</p>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                    {quizzes.map((quiz) => (
                        <Link
                            key={quiz.id}
                            to={`/quizzes/${quiz.id}`}
                            className="block bg-emerald-950/40 border border-emerald-900/50 rounded-2xl p-5 hover:border-emerald-700 transition"
                        >
                            <p className="text-sm text-stone-400">{formatDate(quiz.generatedAt)}</p>
                            <p className="text-lg font-semibold text-emerald-50 mt-1">{quiz.topicTitle}</p>
                            <p className="text-lg font-semibold text-emerald-50 mt-1">
                                Score: {quiz.score !== null ? quiz.score : "Not submitted"}
                            </p>
                        </Link>
                    ))}
                </div>
            )}
        </div>
    );
}

export default QuizHistoryPage;