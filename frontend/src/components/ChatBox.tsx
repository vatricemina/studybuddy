import { useState } from 'react';
import axios from 'axios';
import ReactMarkdown from 'react-markdown';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';
import 'katex/dist/katex.min.css';

function ChatBox({ topicId }) {
    const [messages, setMessages] = useState([]);
    const [inputText, setInputText] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSend() {
        if (inputText.trim() === "") return;

        const userMessage = { role: "user", content: inputText };
        const updatedMessages = [...messages, userMessage];
        setMessages(updatedMessages);
        setInputText("");
        setLoading(true);

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post("http://localhost:8080/api/chat", {
                topicId: topicId ? Number(topicId) : null,
                messages: updatedMessages
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });

            const aiMessage = { role: "assistant", content: response.data };
            setMessages([...updatedMessages, aiMessage]);
        } catch (error) {
            console.log("Error sending message: ", error);
        } finally {
            setLoading(false);
        }
    }

    function handleKeyDown(e) {
        if (e.key === "Enter") handleSend();
    }

    return (
        <div className="flex flex-col h-full">
            <div className="bg-white/80 border border-indigo-100 rounded-2xl shadow-sm flex-1 overflow-y-auto p-4 mb-4">
                {messages.map((message, index) => (
                    <div key={index} className={`my-2 flex ${message.role === "user" ? "justify-end" : "justify-start"}`}>
                        <span className={`px-4 py-2 rounded-2xl text-sm max-w-[85%] ${
                            message.role === "user" ? "bg-indigo-400 text-white" : "bg-slate-100 text-slate-700"
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
                    placeholder="Ask anything..."
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

export default ChatBox;