import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import usePageTitle from "../hooks/PageTitleHook"
import useAuth from "../hooks/AuthHook"
import axios from "axios"
import axiosInstance from "../api/AxiosConfig"
import "../styles/Auth.css"
import EmailIcon from '../assets/email-icon.svg?react'
import PasswordIcon from '../assets/password-icon.svg?react'


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
        <>  
            <div className="auth-wrapper">
                {/* Background blobs */}
                <div className="auth-bg">
                    <div className="auth-bg-blob-top" />
                    <div className="auth-bg-blob-bottom" />
                </div>
                <main className="auth-main">
                    {/* Logo */}
                    <div className="auth-brand">
                        <h1 className="auth-brand-title">
                            <img src="/src/assets/logo-only.svg" alt="MiniTrello Logo" className="auth-brand-title-logo" />
                            MiniTrello
                        </h1>
                        <p className="auth-brand-subtitle">Elevate your productivity flow.</p>
                    </div>
                    {/* Card */}
                    <div className="auth-card">
                        <form className="auth-form" onSubmit={handleSubmit}>
                            <div className="field-group">
                                <label className="field-label">Email Address</label>
                                <div className="field-input-wrapper">
                                    <EmailIcon className="field-icon"/>
                                    <input className="field-input" type="email" placeholder="name@company.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                </div>
                            </div>
                            <div className="field-group">
                                <div className="field-label-row">
                                    <label className="field-label">Password</label>
                                    <Link className="field-forgot" to="#">Forgot?</Link>
                                </div>
                                <div className="field-input-wrapper">
                                    <PasswordIcon className="field-icon"/>
                                    <input className="field-input" type="password" placeholder="••••••••" value={password} onChange={(e) => setPassword(e.target.value)} required />
                                </div>
                            </div>

                            <div className="auth-submit-wrapper">
                                <button className="auth-submit-btn" type="submit">
                                    <span>Sign In</span>
                                </button>
                            </div>
                        </form>
                    </div>
                    <p className="auth-register">
                        New to MiniTrello?
                        <Link to="#">Create an account</Link>
                    </p>
                </main>
                <div className="auth-side-shape">
                    <div className="auth-side-shape-inner" />
                </div>
            </div>
        </>
    )
}
