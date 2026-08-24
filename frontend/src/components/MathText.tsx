import {InlineMath} from 'react-katex';

function MathText({text}){
    const parts = text.split(/(\\\(.*?\\\))/g);

    return (
        <span>
      {parts.map((part, index) => {
          if (part.startsWith("\\(") && part.endsWith("\\)")) {
              const formula = part.slice(2, -2);
              return <InlineMath key={index} math={formula} />;
          }
          return <span key={index}>{part}</span>;
      })}
    </span>
    );
}

export default MathText;