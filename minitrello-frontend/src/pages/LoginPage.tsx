import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import usePageTitle from "../hooks/usePageTitle"
import useAuth from "../hooks/useAuth"
import axios from "axios"
import axiosInstance from "../api/AxiosConfig"
import EmailIcon from '../assets/email-icon.svg?react'
import PasswordIcon from '../assets/password-icon.svg?react'
import AuthField from "../components/auth/AuthField"
import AuthRedirect from "../components/auth/AuthRedirect"


export default function LoginPage() {
    usePageTitle("MiniTrello - Login")
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
            <div className="auth-card">
                <form className="auth-form" onSubmit={handleSubmit}>
                    <AuthField
                        icon={EmailIcon}
                        fieldLabel="Email Address"
                        fieldType="email"
                        fieldPlaceholder="email@provider.com"
                        value={email}
                        onChange={setEmail}
                    />
                    <AuthField
                        icon={PasswordIcon}
                        fieldLabel="Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={password}
                        onChange={setPassword}
                        extra={<Link className="field-forgot" to="#">Forgot?</Link>}
                    />
                    <div className="auth-submit-wrapper">
                        <button className="auth-submit-btn" type="submit">
                            <span>Sign In</span>
                        </button>
                    </div>
                </form>
            </div>
            <AuthRedirect
                footerText="New to MiniTrello?"
                footerLink="/register"
                footerAction="Create an account"
            />
        </>
    )
}
