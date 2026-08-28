import { useState } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import QuizQuestionCard from '../components/QuizQuestionCard';

function QuizPage(){
    const { topicId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [quiz, setQuiz] = useState(location.state?.quiz || null);
    const [answers, setAnswers] = useState({});
    const [showResults, setShowResults] = useState(false);
    const [error, setError] = useState("");


    async function handleGenerate(){
        setLoading(true);
        try{
            const token = localStorage.getItem("token");
            const response = await axios.post(`${import.meta.env.VITE_API_URL}/api/quizzes/generate`, {
                topicId: Number(topicId)
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setQuiz(response.data);
            setAnswers({});
            setShowResults(false);
        }catch(error){
            console.log("Error generating quiz: ", error);
        }finally{
            setLoading(false);
        }
    }

    function handleAnswerSelect(questionId, selectedOption){
        setAnswers({...answers, [questionId]: selectedOption});
    }

    async function handleSubmit(){
        if (Object.keys(answers).length < quiz.questions.length) {
            setError("Please answer all questions before submitting");
            return;
        }

        const answersArray = Object.keys(answers).map((questionId) => ({
            questionId: Number(questionId),
            userAnswer: answers[questionId]
        }));

        const token = localStorage.getItem("token");
        const response = await axios.put(`${import.meta.env.VITE_API_URL}/api/quizzes/${quiz.id}/submit`, {
            answers: answersArray
        }, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setQuiz(response.data);
        setShowResults(true);
    }

    function goToHistory(){
        navigate(`/topics/${topicId}/quiz/history`);
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">
                    Quiz {quiz ? `on ${quiz.topicTitle}` : ""}
                </h1>
                <p className="text-stone-400">Test your knowledge with an AI-generated quiz.</p>
            </div>

            <div className="flex gap-3 mb-8">
                <button
                    onClick={handleGenerate}
                    disabled={loading}
                    className="bg-emerald-700 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition disabled:opacity-50"
                >
                    {loading ? "Generating..." : "Generate new quiz"}
                </button>
                <button
                    onClick={goToHistory}
                    className="bg-transparent border border-emerald-700 text-emerald-300 px-5 py-2 rounded-lg text-sm font-medium hover:bg-emerald-900/40 transition"
                >
                    Previously generated quizzes
                </button>
            </div>

            <div className="space-y-4">
                {quiz && quiz.questions.map((question) => (
                    <QuizQuestionCard
                        key={question.id}
                        question={question}
                        onSelect={handleAnswerSelect}
                        selectedAnswer={answers[question.id]}
                        showResults={showResults}
                    />
                ))}
            </div>
            {error &&  (
                <p className="text-sm text-rose-400 mb-4">{error}</p>
            )}


            {quiz && !showResults && (
                <button
                    onClick={handleSubmit}
                    className="mt-6 bg-rose-700 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-rose-600 transition"
                >
                    Submit Quiz
                </button>
            )}

            {showResults && (
                <h2 className="mt-6 text-xl font-semibold text-emerald-50">
                    Score: {quiz.score} / {quiz.questions.length}
                </h2>
            )}
        </div>
    );
}

export default QuizPage;