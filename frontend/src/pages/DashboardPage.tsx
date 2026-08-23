import { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

function DashboardPage(){
    const [subjects, setSubjects]=useState([]);
    const [name, setName]=useState("");
    const [examDate, setExamDate]=useState("");
    const [difficulty, setDifficulty]=useState("");
    const navigate=useNavigate();

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

    function handleLogout(){
        localStorage.removeItem("token");
        navigate("/login");
    }

    return (
        <div>
            <h1>Dashboard</h1>
            <button onClick={handleLogout}>Logout</button>

            <ul>
                {subjects.map((subject)=> (
                    <li key={subject.id}>
                        <Link to={`/subjects/${subject.id}/topics`}>{subject.name}</Link>
                    </li>
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
            <select value={difficulty} onChange={(e)=> setDifficulty(e.target.value)}>
                <option value=""> Select difficulty</option>
                <option value="1">1 - Very easy</option>
                <option value="2">2 - Easy</option>
                <option value="3">3 - Medium</option>
                <option value="4">4 - Hard</option>
                <option value="5">5 - Very hard</option>
            </select>
            {/*<input*/}
            {/*    type="number"*/}
            {/*    placeholder="Difficulty (1-5)"*/}
            {/*    value={difficulty}*/}
            {/*    onChange={(e)=>setDifficulty(e.target.value)}*/}
            {/*/>*/}
            <button onClick={handleAddSubject}>Add subject</button>


        </div>
    );
}

export default DashboardPage;