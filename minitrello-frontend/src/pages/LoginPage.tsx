import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import usePageTitle from "../hooks/PageTitleHook"
import useAuth from "../hooks/AuthHook"
import axios from "axios"
import axiosInstance from "../api/AxiosConfig"


export default function LoginPage() {
    usePageTitle("MiniTrello - Start Organizing Yourself!")
    const { login } = useAuth()
    const navigate = useNavigate()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault()

        try {
            const response = await axiosInstance.post("/api/auth/login", { email, password })
            
            login(response.data)
            navigate("/dashboard")
        } catch (error) {
            if (axios.isAxiosError(error))
                console.error("Login failed:", error.response?.data.message || error.message)
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
