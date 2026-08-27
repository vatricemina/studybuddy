import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import FlashcardCard from '../components/FlashcardCard';
import { ArrowLeft } from 'lucide-react';


function FlashcardsPage(){
    const {topicId}=useParams();
    const [flashcards, setFlashcards]=useState([]);
    const [loading, setLoading]=useState(false);
    const [error, setError] = useState("");
    const [subjectId, setSubjectId]=useState("");
    const navigate=useNavigate();

    async function fetchExistingFlashcards(){
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/flashcards", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        const filtered=response.data.filter(
            (card)=>card.topicId ===Number(topicId)
        );
        setFlashcards(filtered);
    }

    async function fetchTopicSubject(){ //za vracanje na subjects/subjectId/topics page
        const token=localStorage.getItem("token");
        const response=await axios.get("http://localhost:8080/api/topics", {
            headers: { Authorization: `Bearer ${token}` }
        });
        const topic=response.data.find((t)=>t.id===Number(topicId));
        if(topic) setSubjectId(topic.subjectId);
    }

    useEffect(()=>{
        fetchExistingFlashcards();
        fetchTopicSubject();
    }, [topicId]);

    async function handleGenerate(){
        setLoading(true);
        try{
            const token=localStorage.getItem("token");
            const response=await axios.post("http://localhost:8080/api/flashcards/generate", {
                topicId:Number(topicId)
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setFlashcards((prev)=>[...response.data, ...prev]);
        }catch(error){
            if (error.response && error.response.data && error.response.data.message) {
                setError(error.response.data.message);
            } else {
                setError("Something went wrong. Please try again.");
            }
        }finally{
            setLoading(false);
        }
    }

    return (
        <div>
            <div className="mb-8">
                <h1 className="text-4xl font-extrabold text-emerald-50 mb-2">Flashcards</h1>
                <p className="text-stone-400">Generate AI-powered flashcards and test your memory.</p>
            </div>

            <button
                onClick={handleGenerate}
                disabled={loading}
                className="bg-emerald-700 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition disabled:opacity-50 mb-6"
            >
                {loading ? "Generating..." : "Generate More Flashcards"}
            </button>

            <div className="flex flex-wrap gap-4 mb-10">
                {flashcards.map((card) => (
                    <FlashcardCard key={card.id} question={card.question} answer={card.answer} />
                ))}
            </div>

            {subjectId && (
                <button
                    onClick={() => navigate(`/subjects/${subjectId}/topics`)}
                    className="flex items-center gap-2 text-stone-400 hover:text-emerald-300 text-sm font-medium transition"
                >
                    <ArrowLeft size={16} /> Back to topics page
                </button>
            )}
        </div>
    );
}
export default FlashcardsPage;