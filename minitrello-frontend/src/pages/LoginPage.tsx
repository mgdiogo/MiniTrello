import { useState } from "react"
import { Link } from "react-router-dom"
import axios from "axios"
import usePageTitle from "../hooks/PageTitle"


export default function LoginPage() {
    usePageTitle("MiniTrello - Start Organizing Yourself!");

    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault();

        try {
            const response = await axios.post("/api/auth/login", { email, password });
            
            console.log(response?.data);
        } catch (error) {
            if (axios.isAxiosError(error))
                console.error("Login failed:", error.response?.data.message || error.message);
            else 
                console.error("An unexpected error occurred:", error);
        }
    }

    return (
        <main>
            <div className="login-page">
                <div className="login-container">
                    <form onSubmit={handleSubmit}>
                        <img className="login-container-logo" src="/src/assets/logo-full.svg" alt="MiniTrelloLogin" />
                        <div className="form-group">
                            <input className="form-input" type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                        </div>
                        <div className="form-group">
                            <input className="form-input" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                        </div>
                        <div className="forgot-password">
                            <Link className="a" to="#">
                                Forgot Password?
                            </Link>
                        </div>
                        <div className="button-submit">
                            <button className="button-primary" type="submit">
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    )
}