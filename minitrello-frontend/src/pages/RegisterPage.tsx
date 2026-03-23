import { useNavigate } from "react-router-dom";
import usePageTitle from "../hooks/usePageTitle";
import { useState } from "react";
import axios from "axios";
import axiosInstance from "../api/AxiosConfig";
import AuthField from "../components/AuthField";
import AuthRedirect from "../components/AuthRedirect";

export default function RegisterPage() {
    usePageTitle("MiniTrello - Register")
    const navigate = useNavigate()
    const [fullName, setFullName] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")

    {/*Backend currently does not support full name and confirm password, still needs to be implemented*/ }
    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault()

        try {
            const response = await axiosInstance.post("/api/auth/register", { fullName, email, password, confirmPassword })

            console.log(response)
            navigate("/login")
        } catch (error) {
            if (axios.isAxiosError(error))
                console.error("Login failed:", error.response?.data.message || error.message)
        }
    }

    return (
        <>
            <div className="auth-card">
                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="field-input-row">
                        <AuthField
                            fieldLabel="Full Name"
                            fieldType="text"
                            fieldPlaceholder="John Doe"
                            value={fullName}
                            onChange={setFullName}
                        />
                        <AuthField
                            fieldLabel="Email"
                            fieldType="email"
                            fieldPlaceholder="email@provider.com"
                            value={email}
                            onChange={setEmail}
                        />
                    </div>
                    <AuthField
                        fieldLabel="Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={password}
                        onChange={setPassword}
                    />
                    <AuthField
                        fieldLabel="Confirm Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={confirmPassword}
                        onChange={setConfirmPassword}
                    />
                    <div className="auth-submit-wrapper">
                        <button className="auth-submit-btn" type="submit">
                            <span>Register</span>
                        </button>
                    </div>
                </form>
            </div>
            <AuthRedirect
                footerText="Already have an account?"
                footerLink="/"
                footerAction="Sign in"
            />
        </>
    )
}