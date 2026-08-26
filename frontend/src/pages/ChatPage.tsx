import ChatBox from '../components/ChatBox';

function ChatPage() {
    return (
        <div className="h-[70vh]">
            <div className="mb-6">
                <h1 className="text-4xl font-extrabold text-indigo-600 mb-2">AI Chat</h1>
                <p className="text-slate-500">Ask your AI study buddy anything.</p>
            </div>
            <ChatBox />
        </div>
    );
}

export default ChatPage;