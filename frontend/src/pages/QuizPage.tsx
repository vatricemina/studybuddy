import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import QuizQuestionCard from '../components/QuizQuestionCard';


function QuizPage(){
    const {topicId}=useParams();
    const [loading, setLoading]=useState(false);
    const [quiz, setQuiz]=useState(null);
    const [answers, setAnswers]=useState({}); //pamti sve odgovore za sva pitanja odjednom kao objekat {10:"B", 12:"A"} na pitanje s id 10 odgovorio je sa B, itd..
    const [showResults, setShowResults]=useState(false); //pamti jel kviz predat
    const navigate=useNavigate();


    async function handleGenerate(){
        setLoading(true);
        try{
            const token=localStorage.getItem("token");
            const response=await axios.post("http://localhost:8080/api/quizzes/generate", {
                topicId:Number(topicId)
            }, {
                headers:{
                    Authorization: `Bearer ${token}`
                }
            });
            setQuiz(response.data);
            setAnswers({});  //resetuj ako je vec generisao pa hoce opet
            setShowResults(false); //isto

        }catch(error){
            console.log("Error generating quiz: ", error);
            return;
        }finally{
            setLoading(false);
        }

    }

    function handleAnswerSelect(questionId, selectedOption){
        setAnswers({...answers, [questionId]: selectedOption}); //nadovezuje na listu answers novi odgovor ili mijenja postojeci?
        //pravi nov objekat
        //...answers : [questionId] doda/zamijeni par za ovo konkretno pitanje
    }

    async function handleSubmit(){
        const answersArray=Object.keys(answers).map((questionId)=>({
            questionId: Number(questionId),
            userAnswer: answers[questionId]
        }));
        //object.keys(answers) uzima objekat i vraca niz njegovih kljuceva, ovdje su ti id-evi pitanja
        //.map mapira taj niz u oblik {{qId: -, userAns: -}, {---}, {---}...} ---->OBLIK KOJI BACKEND OCEKUJE U SUBMIT DTO-u!!

        const token=localStorage.getItem("token");
        const response=await axios.put(`http://localhost:8080/api/quizzes/${quiz.id}/submit`,{
            answers:answersArray
        }, {
            headers: {
                Authorization: `Bearer ${token}`
            }
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
                <h1 className="text-4xl font-extrabold text-indigo-600 mb-2">
                    Quiz {quiz ? `on ${quiz.topicTitle}` : ""}
                </h1>
                <p className="text-slate-500">Test your knowledge with an AI-generated quiz.</p>
            </div>

            <div className="flex gap-3 mb-8">
                <button
                    onClick={handleGenerate}
                    disabled={loading}
                    className="bg-indigo-400 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition disabled:opacity-50"
                >
                    {loading ? "Generating..." : "Generate new quiz"}
                </button>
                <button
                    onClick={goToHistory}
                    className="bg-white text-indigo-500 border border-indigo-200 px-5 py-2 rounded-lg text-sm font-medium hover:bg-indigo-50 transition"
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

            {quiz && !showResults && (
                <button
                    onClick={handleSubmit}
                    className="mt-6 bg-rose-400 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-rose-500 transition"
                >
                    Submit Quiz
                </button>
            )}

            {showResults && (
                <h2 className="mt-6 text-xl font-semibold text-slate-700">
                    Score: {quiz.score} / {quiz.questions.length}
                </h2>
            )}
        </div>
    );
}

export default QuizPage;