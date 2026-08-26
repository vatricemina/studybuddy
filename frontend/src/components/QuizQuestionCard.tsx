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

    function getOptionClasses(option) {
        const base = "w-full text-left px-4 py-2 rounded-lg text-sm border transition mb-2";
        const style = getOptionStyle(option);

        if (style.backgroundColor === "lightgreen") return `${base} bg-emerald-100 border-emerald-300 text-emerald-700`;
        if (style.backgroundColor === "lightcoral") return `${base} bg-red-100 border-red-300 text-red-700`;
        if (style.backgroundColor === "lightblue") return `${base} bg-indigo-100 border-indigo-300 text-indigo-700`;
        return `${base} bg-white border-slate-200 text-slate-700 hover:bg-slate-50`;
    }

    return (
        <div className="bg-white/80 border border-indigo-100 rounded-2xl shadow-sm p-5">
            <h3 className="text-slate-800 font-medium mb-3"><MathText text={question.questionText} /></h3>

            <button className={getOptionClasses("A")} onClick={() => handleClick("A")} disabled={showResults}>
                A: <MathText text={question.optionA} />
            </button>
            <button className={getOptionClasses("B")} onClick={() => handleClick("B")} disabled={showResults}>
                B: <MathText text={question.optionB} />
            </button>
            <button className={getOptionClasses("C")} onClick={() => handleClick("C")} disabled={showResults}>
                C: <MathText text={question.optionC} />
            </button>
            <button className={getOptionClasses("D")} onClick={() => handleClick("D")} disabled={showResults}>
                D: <MathText text={question.optionD} />
            </button>
        </div>
    );

}
export default QuizQuestionCard;