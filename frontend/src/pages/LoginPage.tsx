import {useState} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';

function LoginPage(){
    const [email, setEmail]=useState("");
    const [password, setPassword]=useState("");
    const navigate=useNavigate();

    async function handleLogin(){
        try{
            const response=await axios.post("http://localhost:8080/api/auth/login", {
                email:email,
                password: password
            });

            localStorage.setItem("token", response.data.token);
            navigate("/dashboard");

        }catch(error){
            console.log("Greska pri loginu: ", error);
        }
    }


    return(
        <div>
            <h1>Login</h1>
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e)=> setEmail(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e)=> setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Prijavi se</button>

        </div>
    );
}

export default LoginPage;