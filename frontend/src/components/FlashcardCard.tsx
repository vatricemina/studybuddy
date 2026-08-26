import { useState } from 'react';

function FlashcardCard({question, answer}){
    const [isFlipped, setIsFlipped]=useState(false);

    function handleClick(){
        setIsFlipped(!isFlipped);
    }

    return (
        <div
            onClick={handleClick}
            className="bg-white/80 border border-indigo-100 rounded-2xl shadow-sm p-6 w-64 cursor-pointer hover:shadow-md transition flex items-center justify-center text-center min-h-[140px]"
        >
            <p className="text-slate-700">{isFlipped ? answer : question}</p>
        </div>
    );
}

export default FlashcardCard;