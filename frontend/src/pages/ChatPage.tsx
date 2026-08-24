import { useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import MathText from '../components/MathText';
import ReactMarkdown from 'react-markdown';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';
import 'katex/dist/katex.min.css';

function ChatPage(){
    const {topicId}=useParams();
    const [messages, setMessages]=useState([]);
    const [inputText, setInputText]=useState("");
    const [loading,setLoading]=useState(false);

    async function handleSend(){
        if(inputText.trim()=="") return;
        const userMessage={role: "user", content:inputText};
        const updatedMessages=[...messages, userMessage];
        setMessages(updatedMessages);
        setInputText("");
        setLoading(true);

        try{
            const token=localStorage.getItem("token");
            const response=await axios.post("http://localhost:8080/api/chat",{
                topicId: Number(topicId),
                messages: updatedMessages
            },{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            const aiMessage={role:"assistant", content: response.data};
            setMessages([...updatedMessages, aiMessage]);

        }catch(error){
            console.log("Error sending message: ", error);
        } finally {
            setLoading(false);
        }
    }

    function handleKeyDown(e){
        if(e.key==="Enter") handleSend();
    }

    return (
        <div>
            <h1>Chat</h1>
            <div style={{ border: "1px solid black", height: "400px", overflowY: "auto", padding: "10px" }}>
                {messages.map((message,index)=>(
                    <div key={index} style={{
                        textAlign: message.role === "user" ? "right" : "left",
                        margin: "10px 0"
                    }}>
                        <span style={{
                            backgroundColor: message.role === "user" ? "lightblue" : "lightgray",
                            padding: "8px 12px",
                            borderRadius: "10px",
                            display: "inline-block"
                        }}>
                            <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                                {message.content}
                            </ReactMarkdown>
                        </span>
                    </div>
                ))}
                {loading && <p>AI is typing...</p>}
            </div>

            <input
                type="text"
                value={inputText}
                onChange={(e)=>setInputText(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Type your question..."
                style={{ width: "80%" }}
            />
            <button onClick={handleSend} disabled={loading}>Send</button>
        </div>
    );
}

export default ChatPage;