import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import QuizQuestionCard from '../components/QuizQuestionCard';

function QuizDetailPage() {
    const {quizId}=useParams();
    const [quiz, setQuiz]=useState(null);

    async function fetchQuiz(){
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/quizzes", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        const found=response.data.find((q)=> q.id==Number(quizId));
        setQuiz(found);
    }

    useEffect(()=>{
        fetchQuiz();
    }, [quizId]);

    if(!quiz){
        return <p>Loading...</p>;
    }

    if (!quiz) {
        return <p className="text-slate-500">Loading...</p>;
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">Quiz on {quiz.topicTitle}</h1>
                <p className="text-slate-500">{formatDate(quiz.generatedAt)}</p>
                <h2 className="text-xl font-semibold text-slate-700 mt-2">
                    Score: {quiz.score} / {quiz.questions.length}
                </h2>
            </div>

            <div className="space-y-4">
                {quiz.questions.map((question) => (
                    <QuizQuestionCard
                        key={question.id}
                        question={question}
                        onSelect={() => {}}
                        selectedAnswer={question.userAnswer}
                        showResults={true}
                    />
                ))}
            </div>
        </div>
    );
}

export default QuizDetailPage;