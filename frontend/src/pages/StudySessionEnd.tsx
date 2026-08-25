import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function StudySessionEnd({cyclesCompleted, totalSecondsElapsed, status}){
    const navigate=useNavigate();

    function handleButton(){
        navigate("/dashboard");
    }

    return(
        <div>
            <h1>Good job!</h1>
            <h2>You studied for {totalSecondsElapsed} minutes with {cyclesCompleted} completed study-break cycles.</h2>
            <p>Study session status: {status}</p>
            <button onClick={handleButton}>Back to dashboard</button>
        </div>
    )
}

export default StudySessionEnd;