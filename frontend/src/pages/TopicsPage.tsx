import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function TopicsPage(){
    const {subjectId} = useParams();
    const [topics, setTopics]=useState([]);
    const [title,setTitle]=useState("");
    const [estimatedHours,setEstimatedHours]=useState("");

    async function fetchTopics(){
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/topics",{
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        const filteredTopics=response.data.filter(
            (topic)=>topic.subjectId==Number(subjectId)
        );
        setTopics(filteredTopics);
    }

    useEffect(()=>{
        fetchTopics();
    }, [subjectId]);

    async function handleAddTopic(){
        const token=localStorage.getItem("token");

        await axios.post("http://localhost:8080/api/topics", {
            title:title,
            estimatedHours:Number(estimatedHours),
            completed:false,
            subjectId:Number(subjectId)
        }, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setTitle("");
        setEstimatedHours("");
        fetchTopics();
    }

    return (
        <div>
            <h1>Topics</h1>
            <ul>
                {topics.map((topic)=>(
                    <li key={topic.id}>{topic.title}</li>
                ))}
            </ul>

            <h2>Add new topic</h2>
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e)=>setTitle(e.target.value)}
            />
            <input
                type="number"
                placeholder="Estimated hours"
                value={estimatedHours}
                onChange={(e)=>setEstimatedHours(e.target.value)}
            />
            <button onClick={handleAddTopic}>Add topic</button>
        </div>
    );
}

export default TopicsPage;