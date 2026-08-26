import {useState} from 'react';
import axios from 'axios';
import {useNavigate, Link} from 'react-router-dom';

function RegisterPage(){
    const [name, setName]=useState("");
    const [lastName, setLastName]=useState("");
    const [email, setEmail]=useState("");
    const [password, setPassword]=useState("");
    const [error, setError]=useState("");
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
            if(error.response && error.response.data && error.response.data.message){
                setError(error.response.data.message);
            }
            else{
                setError("Something went wrong. Please try again.");
            }
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 via-rose-50 to-indigo-50">            <div className="bg-white p-8 rounded-2xl shadow-md w-full max-w-sm">
                <h1 className="text-3xl font-bold text-indigo-500 mb-2 text-center">StudyBuddy</h1>
                <p className="text-sm text-slate-500 mb-6 text-center">
                    Create your account and start studying smarter today.
                </p>

                <input
                    type="text"
                    placeholder="First name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <input
                    type="text"
                    placeholder="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-3 focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-indigo-300"
                />
                {error && (
                    <p className="text-sm text-red-500 mb-4 text-center">{error}</p>
                )}
                <button
                    onClick={handleRegister}
                    className="w-full bg-indigo-400 text-white py-2 rounded-lg font-medium hover:bg-indigo-500 transition"
                >
                    Register
                </button>

                <p className="text-sm text-slate-500 text-center mt-4">
                    Already have an account?{" "}
                    <Link to="/login" className="text-indigo-500 font-medium hover:underline">
                        Log in
                    </Link>
                </p>
            </div>
        </div>
    );
}
export default RegisterPage;