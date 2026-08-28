import { useState } from 'react';
import axios from 'axios';
import ReactMarkdown from 'react-markdown';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';
import 'katex/dist/katex.min.css';

interface Message {
    role: string;
    content: string;
}

function ChatBox({ topicId }: { topicId?: number | string | null }) {
    const [messages, setMessages] = useState<Message[]>([]);
    const [inputText, setInputText] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSend() {
        if (inputText.trim() === "") return;

        const userMessage: Message = { role: "user", content: inputText };
        const updatedMessages = [...messages, userMessage];
        setMessages(updatedMessages);
        setInputText("");
        setLoading(true);

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(`${import.meta.env.VITE_API_URL}/api/chat`, {
                topicId: topicId ? Number(topicId) : null,
                messages: updatedMessages
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });

            const aiMessage: Message = { role: "assistant", content: response.data };
            setMessages([...updatedMessages, aiMessage]);
        } catch (error) {
            console.log("Error sending message: ", error);
        } finally {
            setLoading(false);
        }
    }

    function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
        if (e.key === "Enter") handleSend();
    }

    return (
        <div className="flex flex-col h-full">
            <div className="bg-emerald-950/40 border border-emerald-900/50 rounded-2xl shadow-sm flex-1 overflow-y-auto overflow-x-hidden p-4 mb-4 min-h-0">
                {messages.map((message, index) => (
                    <div key={index} className={`my-2 flex ${message.role === "user" ? "justify-end" : "justify-start"}`}>
                        <span className={`px-4 py-2 rounded-2xl text-sm max-w-[85%] break-words ${
                            message.role === "user" ? "bg-emerald-700 text-white" : "bg-neutral-900 text-emerald-50 border border-emerald-900/50"
                        }`}>
                            <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
                                {message.content}
                            </ReactMarkdown>
                        </span>
                    </div>
                ))}
                {loading && <p className="text-stone-500 text-sm">AI is typing...</p>}
            </div>

            <div className="flex gap-2">
                <input
                    type="text"
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Ask anything..."
                    className="flex-1 bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <button
                    onClick={handleSend}
                    disabled={loading}
                    className="bg-emerald-700 text-white px-5 py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition disabled:opacity-50"
                >
                    Send
                </button>
            </div>
        </div>
    );
}

export default ChatBox;