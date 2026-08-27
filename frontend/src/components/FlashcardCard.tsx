import { useState } from 'react';

function FlashcardCard({question, answer}){
    const [isFlipped, setIsFlipped]=useState(false);

    function handleClick(){
        setIsFlipped(!isFlipped);
    }

    return (
        <div
            onClick={handleClick}
            className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl shadow-sm p-6 w-64 cursor-pointer hover:border-emerald-700 transition flex items-center justify-center text-center min-h-[140px]"
        >
            <p className="text-emerald-50">{isFlipped ? answer : question}</p>
        </div>
    );
}

export default FlashcardCard;