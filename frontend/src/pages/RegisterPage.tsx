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
            const response=await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/register`, {
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
        <div className="min-h-screen flex items-center justify-center bg-neutral-950">
            <div className="bg-emerald-950/40 border border-emerald-900/50 p-8 rounded-2xl shadow-md w-full max-w-sm">
                <h1 className="text-3xl font-bold text-emerald-50 mb-2 text-center">StudyBuddy</h1>
                <p className="text-sm text-stone-400 mb-6 text-center">
                    Create your account and start studying smarter today.
                </p>

                <input
                    type="text"
                    placeholder="First name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <input
                    type="text"
                    placeholder="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-3 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full bg-neutral-900 border border-emerald-900/50 rounded-lg px-4 py-2 mb-4 text-sm text-emerald-50 placeholder-stone-500 focus:outline-none focus:ring-2 focus:ring-emerald-700"
                />

                {error && (
                    <p className="text-sm text-rose-400 mb-4 text-center">{error}</p>
                )}

                <button
                    onClick={handleRegister}
                    className="w-full bg-emerald-700 text-white py-2 rounded-lg text-sm font-medium hover:bg-emerald-600 transition"
                >
                    Register
                </button>

                <p className="text-sm text-stone-400 text-center mt-4">
                    Already have an account?{" "}
                    <Link to="/login" className="text-emerald-400 font-medium hover:underline">
                        Log in
                    </Link>
                </p>
            </div>
        </div>
    );
}
export default RegisterPage;