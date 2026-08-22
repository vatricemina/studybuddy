import { useState, useEffect } from 'react';
import axios from 'axios';

function DashboardPage(){
    const [subjects, setSubjects]=useState([]);
    const [name, setName]=useState("");
    const [examDate, setExamDate]=useState("");
    const [difficulty, setDifficulty]=useState("");

    async function fetchSubjects(){
        const token=localStorage.getItem("token");
        const response= await axios.get("http://localhost:8080/api/subjects", {
            headers:{
                Authorization:`Bearer ${token}`
            }
        });
        setSubjects(response.data);
    }


    useEffect(()=>{
        fetchSubjects();
    }, []);

    async function handleAddSubject(){
        const difficultyNumber=Number(difficulty);
        if(difficultyNumber<1 || difficultyNumber>5){
            alert ("Difficulty must be between 1 and 5");
            return;
        }
        const today=new Date().toISOString().split("T")[0]; //uzme tr datum, pretvori u string, splita ga da odvoji vrijeme (time) i ostane samo datum i uzme prvi dio, ostane samo datum bez vremena
        if(examDate<today){
            alert("Exam date cannot be in the past");
            return;
        }

        const token=localStorage.getItem("token");
        await axios.post("http://localhost:8080/api/subjects", {
            name:name,
            examDate:examDate,
            difficulty:Number(difficulty)
        }, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setName("");
        setExamDate("");
        setDifficulty("");
        fetchSubjects();
    }

    return (
        <div>
            <h1>Dashboard</h1>

            <ul>
                {subjects.map((subject)=> (
                    <li key={subject.id}>{subject.name}</li>
                ))}
            </ul>

            <h2>Add new subject</h2>
            <input
                type="text"
                placeholder="Subject name"
                value={name}
                onChange={(e)=>setName(e.target.value)}
            />
            <input
                type="date"
                placeholder="Exam date"
                value={examDate}
                onChange={(e)=>setExamDate(e.target.value)}
            />
            <input
                type="number"
                placeholder="Difficulty (1-5)"
                value={difficulty}
                onChange={(e)=>setDifficulty(e.target.value)}
            />
            <button onClick={handleAddSubject}>Add subject</button>


        </div>
    );
}

export default DashboardPage;