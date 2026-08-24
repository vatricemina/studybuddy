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
            <h1>Quiz {quiz ? `on topic ${quiz.topicTitle}` : ""}</h1>
            <button onClick={handleGenerate} disabled={loading}>{loading ? "Generating...":"Generate new quiz"}</button>
            <button onClick={goToHistory}>Previously generated quizzes</button>
            <div>
                {quiz && quiz.questions.map((question)=> (
                    <QuizQuestionCard
                        key={question.id}
                        question={question} //cijeli objekt pitanja = questionText+optionABCD+correctAnswer+id
                        onSelect={handleAnswerSelect} //funkciju koju dijete poziva kad klikne
                        selectedAnswer={answers[question.id]} //sta je trenutno izabrano za bas ovo pitanje
                        showResults={showResults} //jel predat kviz
                    />
                ))}
            </div>
            {quiz && !showResults && (
                <button onClick={handleSubmit}>Submit quiz</button>
            )}
            {showResults && (
                <h2>Score: {quiz.score} / {quiz.questions.length}</h2>
            )}
        </div>
    )
}

export default QuizPage;