import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function StudyPlanPage() {
    const {subjectId}=useParams();
    const [plan, setPlan]=useState([]);
    const [loading, setLoading]=useState(false);

    async function fetchPlan(){
        const token=localStorage.getItem("token");
        const response=await axios.get(`http://localhost:8080/api/study-plan/${subjectId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        setPlan(response.data);
    }

    useEffect(() => {
        fetchPlan();
    }, [subjectId]);

    async function handleGenerate(){
        setLoading(true);
        try{
            const token=localStorage.getItem("token");
            const response=await axios.post("http://localhost:8080/api/study-plan/generate", {
                subjectId:Number(subjectId)
            },{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setPlan(response.data);
        }catch(error){
            console.log("Error generating study plan: ", error);
        }finally{
            setLoading(false);
        }

    }

    return(
        <div>
            <h1>Study plan</h1>
            <button onClick={handleGenerate} disabled={loading}>{loading ? "Generating...":"Generate New Plan"}</button>
            <div>
                {plan.map((entry)=>(
                    <div key={entry.id} style={{ border: "1px solid black", padding: "10px", margin: "10px" }}>
                        <p><strong>{entry.plannedDate}</strong> - {entry.topicTitle} ({entry.plannedHours}h-)</p>
                        <p>{entry.focus}</p>
                        <Link to={`/topics/${entry.topicId}/study-session`}>Start study session</Link>
                    </div>
                ))}
            </div>
        </div>
    );

}
export default StudyPlanPage;