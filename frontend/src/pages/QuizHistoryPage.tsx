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
            <h1>Quiz history</h1>
            <div style={{ display: "flex", flexWrap: "wrap" }}>
                {quizzes.map((quiz)=>(
                    <Link key={quiz.id} to={`/quizzes/${quiz.id}`} style={{ textDecoration: "none", color: "black" }}>
                        <div style={{ border: "1px solid black", padding: "15px", margin: "10px", width: "200px" }}>
                            <p>Generated: {formatDate(quiz.generatedAt)}</p>
                            <p>Score: {quiz.score!=null ? quiz.score : "Not submitted"}</p>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
}

export default QuizHistoryPage;