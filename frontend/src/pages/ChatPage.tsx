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
            <div className="mb-6">
                <h1 className="text-3xl font-bold text-slate-800 mb-2">Chat</h1>
                <p className="text-slate-500">Ask your AI study buddy anything about this topic.</p>
            </div>

            <div className="bg-white/80 border border-indigo-100 rounded-2xl shadow-sm h-96 overflow-y-auto p-4 mb-4">
                {messages.map((message, index) => (
                    <div key={index} className={`my-2 flex ${message.role === "user" ? "justify-end" : "justify-start"}`}>
                    <span className={`px-4 py-2 rounded-2xl text-sm max-w-[75%] ${
                        message.role === "user"
                            ? "bg-indigo-400 text-white"
                            : "bg-slate-100 text-slate-700"
                    }`}>
                        <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                            {message.content}
                        </ReactMarkdown>
                    </span>
                    </div>
                ))}
                {loading && <p className="text-slate-400 text-sm">AI is typing...</p>}
            </div>

            <div className="flex gap-2">
                <input
                    type="text"
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Type your question..."
                    className="flex-1 border border-slate-200 rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <button
                    onClick={handleSend}
                    disabled={loading}
                    className="bg-indigo-400 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition disabled:opacity-50"
                >
                    Send
                </button>
            </div>
        </div>
    );
}

export default ChatPage;