import { useLayoutEffect, useState } from "react"
import { Link } from "react-router-dom"
import axios from "axios"


export default function HomePage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    useLayoutEffect(() => {
        document.title = "MiniTrello - Start Organizing Yourself!"
    }, [])

    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault();

        try {
            const response = await axios.post("/api/auth/login", { email, password });
            
            console.log(response.data);
        } catch (error) {
            if (axios.isAxiosError(error))
                console.error("Login failed:", error.response?.data.message || error.message);
        }
    }

    return (
        <main>
            <div className="login-page">
                <div className="login-container">
                    <form onSubmit={handleSubmit}>
                        <h1>Login</h1>
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