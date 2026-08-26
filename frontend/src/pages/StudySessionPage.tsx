import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import StudySessionEnd from './StudySessionEnd';

function StudySessionPage() {
    const{topicId}=useParams();
    const navigate=useNavigate();

    const [sessionStarted, setSessionStarted]=useState(false);
    const [studyMinutes, setStudyMinutes]=useState(25);
    const [breakMinutes, setBreakMinutes] = useState(5);
    const [plannedMinutes, setPlannedMinutes] = useState(50);
    const [secondsLeft, setSecondsLeft]=useState(0);
    const [cyclesCompleted, setCyclesCompleted]=useState(0);
    const [isBreak, setIsBreak]=useState(false);
    const [sessionId, setSessionId] = useState(null);
    const [totalSecondsElapsed, setTotalSecondsElapsed] = useState(0);

    const [sessionEnded, setSessionEnded]=useState(false);
    const [sessionStatus, setSessionStatus]=useState("");



   useEffect(()=>{
       if(!sessionStarted){
           return;
       }
       const intervalId=setInterval(()=>{
           setSecondsLeft((prev)=>{
               if(prev<=1){ //ako je preostalo vrijeme skoro 0
                   if(isBreak){ //ako je bila pauza, predji na rad
                       setIsBreak(false);
                       setCyclesCompleted((prevCycles)=>prevCycles+1);
                       return studyMinutes*60;
                   }else{
                       setIsBreak(true); //ako je bio rad, predji na pauzu
                       return breakMinutes*60;
                   }
               }
               return prev-1; //inace (vrijeme nije isteklo), samo oduzmi 1
           });
           setTotalSecondsElapsed((prev)=>prev+1); //svaku sekundu povecaj ukupan brojac vremena za 1
       },1000);
       return ()=>{ //cleanup-kad korisnik napusti stranici, ili se useeffect ponovo pokrene jer se u nizu nesto promijenilo, pozove se ova fja
           clearInterval(intervalId); //zaustavlja stari interval
       };
   }, [sessionStarted, isBreak]);



    async function handleStart(){
        const token=localStorage.getItem("token");
        const response=await axios.post("http://localhost:8080/api/study-sessions", {
            topicId: Number(topicId),
            studyIntervalMinutes: studyMinutes,
            breakIntervalMinutes: breakMinutes,
            plannedDurationMinutes: plannedMinutes,
            status: "IN_PROGRESS",
            startedAt: new Date().toISOString()
        }, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        setSessionStarted(true);
        setSessionId(response.data.id);
        setSecondsLeft(studyMinutes*60);
        setIsBreak(false);
        setCyclesCompleted(0);
        setTotalSecondsElapsed(0);
    }

    async function handleEnd(){
        const token = localStorage.getItem("token");
        const actualMinutes=Math.floor(totalSecondsElapsed/60);
        const status=actualMinutes>=plannedMinutes ? "COMPLETED" : "ABANDONED";

        await axios.put(`http://localhost:8080/api/study-sessions/${sessionId}`, {
            topicId: Number(topicId),
            studyIntervalMinutes: studyMinutes,
            breakIntervalMinutes: breakMinutes,
            plannedDurationMinutes: plannedMinutes,
            actualDurationMinutes: actualMinutes,
            cyclesCompleted: cyclesCompleted-1,
            status: status,
            endedAt: new Date().toISOString()
        }, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        setSessionStatus(status);
        setSessionStarted(false);
        setSessionEnded(true);

    }

    function formatTime(secs){
        const minutes=Math.floor(secs/60);
        const seconds=secs%60;
        return `${minutes}:${seconds<10 ? "0" : ""}${seconds}`;
    }

    if(sessionEnded){
        return (<StudySessionEnd
            cyclesCompleted={cyclesCompleted}
            totalSecondsElapsed={Math.floor(totalSecondsElapsed/60)}
            status={sessionStatus}
        />);
    }

    if (!sessionStarted) {
        return (
            <div>
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-slate-800 mb-2">Study Session</h1>
                    <p className="text-slate-500">Set your intervals and start a focused study session.</p>
                </div>

                <div className="bg-white/80 p-6 rounded-2xl shadow-sm border border-indigo-100 max-w-md">
                    <label className="block text-sm text-slate-600 mb-1">Study interval (min)</label>
                    <input
                        type="number"
                        value={studyMinutes}
                        onChange={(e) => setStudyMinutes(Number(e.target.value))}
                        className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                    />
                    <label className="block text-sm text-slate-600 mb-1">Break interval (min)</label>
                    <input
                        type="number"
                        value={breakMinutes}
                        onChange={(e) => setBreakMinutes(Number(e.target.value))}
                        className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                    />
                    <label className="block text-sm text-slate-600 mb-1">Planned total (min)</label>
                    <input
                        type="number"
                        value={plannedMinutes}
                        onChange={(e) => setPlannedMinutes(Number(e.target.value))}
                        className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
                    />
                    <button
                        onClick={handleStart}
                        className="w-full bg-indigo-400 text-white py-2 rounded-lg text-sm font-medium hover:bg-indigo-500 transition"
                    >
                        Start
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="text-center">
            <h1 className="text-2xl font-bold text-slate-800 mb-6">{isBreak ? "Break" : "Study"} Time</h1>

            <div className={`w-52 h-52 rounded-full border-8 mx-auto flex items-center justify-center text-4xl font-semibold ${
                isBreak ? "border-rose-300 text-rose-500" : "border-indigo-300 text-indigo-500"
            }`}>
                {formatTime(secondsLeft)}
            </div>

            <p className="text-slate-500 mt-6">Cycles completed: {cyclesCompleted}</p>

            <button
                onClick={handleEnd}
                className="mt-6 bg-rose-400 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-rose-500 transition"
            >
                End Session
            </button>
        </div>
    );


}
export default StudySessionPage;