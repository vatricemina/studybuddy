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
   }, [sessionStarted]);



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

    if(!sessionStarted){
        return(
            <div>
                <h1>Study Session</h1>
                <label>Study interval (min): </label>
                <input type="number" value={studyMinutes} onChange={(e)=>setStudyMinutes(e.target.value)}/>
                <br/>
                <label>Break interval (min): </label>
                <input type="number" value={breakMinutes} onChange={(e)=>setBreakMinutes(e.target.value)}/>
                <br/>
                <label>Planned minutes (min): </label>
                <input type="number" value={plannedMinutes} onChange={(e)=>setPlannedMinutes(e.target.value)}/>
                <br/>
                <button onClick={handleStart}>Start</button>
            </div>
        );
    }

    return(
        <div>
            <h1> {isBreak?"Break":"Study"} Time</h1>
            <div style={{
                width: "200px",
                height: "200px",
                borderRadius: "50%",
                border: "5px solid black",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontSize: "32px",
                margin: "20px auto"
            }}>
                {formatTime(secondsLeft)}

            </div>
            <p>Cycles completed: {cyclesCompleted}</p>
            <div style={{ textAlign: "right" }}>
                <button onClick={handleEnd}>End Session</button>
            </div>
        </div>
    );


}
export default StudySessionPage;