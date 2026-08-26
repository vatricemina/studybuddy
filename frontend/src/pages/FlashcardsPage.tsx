import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import FlashcardCard from '../components/FlashcardCard';

function FlashcardsPage(){
    const {topicId}=useParams();
    const [flashcards, setFlashcards]=useState([]);
    const [loading, setLoading]=useState(false);
    const [error, setError] = useState("");

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

    useEffect(()=>{
        fetchExistingFlashcards();
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

            setFlashcards(response.data);
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
                <h1 className="text-4xl font-extrabold text-indigo-600 mb-2">Flashcards</h1>
                <p className="text-slate-500">Generate AI-powered flashcards and test your memory.</p>
            </div>

            <button
                onClick={handleGenerate}
                disabled={loading}
                className="bg-indigo-400 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition disabled:opacity-50 mb-6"
            >
                {loading ? "Generating..." : "Generate Flashcards"}
            </button>
            {error && (
                <p className="text-sm text-red-500 mb-4">{error}</p>
            )}

            <div className="flex flex-wrap gap-4">
                {flashcards.map((card) => (
                    <FlashcardCard key={card.id} question={card.question} answer={card.answer} />
                ))}
            </div>
        </div>
    );
}
export default FlashcardsPage;