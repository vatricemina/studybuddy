import {useState} from 'react';
import axios from 'axios';

function LoginPage(){
    const [email, setEmail]=useState("");
    const [password, setPassword]=useState("");

    async function handleLogin(){
        try{
            const response=await axios.post("http://localhost:8080/api/auth/login", {
                email:email,
                password: password
            });

            console.log("uspjesno! token: ", response.data.token);

        }catch(error){
            console.log("greska: ", error);
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
            <button onClick={handleLogin}> prijavi se</button>

        </div>
    );
}

export default LoginPage;