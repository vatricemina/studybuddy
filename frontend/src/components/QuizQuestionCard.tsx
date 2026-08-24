import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import MathText from './MathText';


function QuizQuestionCard({question, onSelect, selectedAnswer, showResults}){

    function handleClick(option){
        onSelect(question.id, option);
    }

    function getOptionStyle(option){
       if(!showResults){
           if (option === selectedAnswer) return { backgroundColor: "lightblue" };
           return {};
       }
       if(option===question.correctAnswer) return {backgroundColor:"lightgreen"};
       if (option === selectedAnswer && option !== question.correctAnswer) return { backgroundColor: "lightcoral" };
       return {};
    }

    return (
        <div style={{ border: "1px solid black", padding: "15px", margin: "10px" }}>
            <h3><MathText text={question.questionText} /></h3>
            <button style={getOptionStyle("A")} onClick={() => handleClick("A")} disabled={showResults}>A: <MathText text={question.optionA} /></button>
            <button style={getOptionStyle("B")} onClick={() => handleClick("B")} disabled={showResults}>
                B: <MathText text={question.optionB} />
            </button>
            <button style={getOptionStyle("C")} onClick={() => handleClick("C")} disabled={showResults}>
                C: <MathText text={question.optionC} />
            </button>
            <button style={getOptionStyle("D")} onClick={() => handleClick("D")} disabled={showResults}>
                D: <MathText text={question.optionD} />
            </button>
        </div>
    );

}
export default QuizQuestionCard;