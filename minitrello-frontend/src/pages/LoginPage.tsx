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

type LoginErrors = {
    email?: string
    password?: string
    form?: string
}

export default function LoginPage() {
    usePageTitle("MiniTrello - Login")
    const { login } = useAuth()
    const navigate = useNavigate()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [errors, setErrors] = useState<LoginErrors>({})

    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault()

        try {
            const response = await axiosInstance.post("/api/auth/login", { email, password })

            login(response.data)
            navigate("/dashboard")
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const message = error.response?.data.message;
                const field = error.response?.data.field;

                if (field)
                    setErrors({ [field]: message })
                else
                    setErrors({ form: "Something went wrong. Please try again later." })
            }
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
                        errorMessage={errors.email}
                    />
                    <AuthField
                        icon={PasswordIcon}
                        fieldLabel="Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={password}
                        onChange={setPassword}
                        extra={<Link className="field-forgot" to="#">Forgot?</Link>}
                        errorMessage={errors.password}
                    />
                    {errors.form && <p className="auth-error-form">{errors.form}</p>}
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
