import MathText from './MathText';

function getOptionClasses(option, question, selectedAnswer, showResults) {
    const base = "w-full text-left px-4 py-2 rounded-lg text-sm border transition mb-2";

    if (!showResults) {
        if (option === selectedAnswer) return `${base} bg-emerald-900/60 border-emerald-600 text-emerald-200`;
        return `${base} bg-neutral-900 border-emerald-900/50 text-emerald-50 hover:bg-emerald-950/50`;
    }

    if (option === question.correctAnswer) return `${base} bg-emerald-800/60 border-emerald-500 text-emerald-100`;
    if (option === selectedAnswer && option !== question.correctAnswer) return `${base} bg-rose-900/60 border-rose-600 text-rose-200`;
    return `${base} bg-neutral-900 border-emerald-900/50 text-stone-400`;
}

function QuizQuestionCard({ question, onSelect, selectedAnswer, showResults }) {
    function handleClick(option) {
        onSelect(question.id, option);
    }

    return (
        <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl shadow-sm p-5">
            <h3 className="text-emerald-50 font-medium mb-3"><MathText text={question.questionText} /></h3>

            <button className={getOptionClasses("A", question, selectedAnswer, showResults)} onClick={() => handleClick("A")} disabled={showResults}>
                A: <MathText text={question.optionA} />
            </button>
            <button className={getOptionClasses("B", question, selectedAnswer, showResults)} onClick={() => handleClick("B")} disabled={showResults}>
                B: <MathText text={question.optionB} />
            </button>
            <button className={getOptionClasses("C", question, selectedAnswer, showResults)} onClick={() => handleClick("C")} disabled={showResults}>
                C: <MathText text={question.optionC} />
            </button>
            <button className={getOptionClasses("D", question, selectedAnswer, showResults)} onClick={() => handleClick("D")} disabled={showResults}>
                D: <MathText text={question.optionD} />
            </button>
        </div>
    );
}

export default QuizQuestionCard;