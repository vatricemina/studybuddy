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

    return (
        <div>
            <h1>Quiz on topic {quiz.topicTitle}</h1>
            <h2>Score: {quiz.score} / {quiz.questions.length}</h2>

            <div>
                {quiz.questions.map((question)=>(
                    <QuizQuestionCard
                        key={question.id}
                        question={question}
                        onSelect={()=>{}}
                        selectedAnswer={question.userAnswer}
                        showResults={true}
                    />
                ))}
            </div>
        </div>
    )
}

export default QuizDetailPage;