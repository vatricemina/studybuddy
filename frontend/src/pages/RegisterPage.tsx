import {useState} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';

function RegisterPage(){
    const [name, setName]=useState("");
    const [lastName, setLastName]=useState("");
    const [email, setEmail]=useState("");
    const [password, setPassword]=useState("");
    const navigate=useNavigate();

    async function handleRegister(){
        try{
            const response=await axios.post("http://localhost:8080/api/auth/register", {
                firstName: name,
                lastName: lastName,
                email:email,
                password:password
            });

            localStorage.setItem("token", response.data.token);
            navigate("/dashboard");
        }catch(error){
            console.log("Greska pri registraciji: ", error);
        }
    }

    return (
        <div>
            <h1>Register</h1>
            <input
                type="text"
                placeholder="First name"
                value={name}
                onChange={(e)=>setName(e.target.value)}
            />
            <input
                type="text"
                placeholder="Last name"
                value={lastName}
                onChange={(e)=>setLastName(e.target.value)}
            />
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e)=>setEmail(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e)=>setPassword(e.target.value)}
            />
            <button onClick={handleRegister}>Sign up</button>

        </div>
    );
}
export default RegisterPage;