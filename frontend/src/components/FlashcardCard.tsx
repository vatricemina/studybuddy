import { useState } from 'react';

function FlashcardCard({question, answer}){
    const [isFlipped, setIsFlipped]=useState(false);

    function handleClick(){
        setIsFlipped(!isFlipped);
    }

    return (
        <div onClick={handleClick} style={{
            border: "1px solid black",
            padding: "20px",
            margin: "10px",
            cursor: "pointer",
            width: "300px"
        }}>
            {isFlipped ? <o>{answer}</o> : <p>{question}</p>}

        </div>
    );
}

export default FlashcardCard;