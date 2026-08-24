import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import FlashcardCard from '../components/FlashcardCard';

function FlashcardsPage(){
    const {topicId}=useParams();
    const [flashcards, setFlashcards]=useState([]);
    const [loading, setLoading]=useState(false);

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
            console.log("Error generating flashcards:", error);
        }finally{
            setLoading(false);
        }
    }

    return (
        <div>
            <h1>Flashcards</h1>
            <button onClick={handleGenerate} disabled={loading}> {loading ? "Generating..." : "Generate Flashcards"}</button>

            <div style={{display:"flex", flexWrap:"wrap"}}>
                {flashcards.map((card)=>(
                    <FlashcardCard key={card.id} question={card.question} answer={card.answer}/>
                ))}
            </div>
        </div>


    )
}
export default FlashcardsPage;