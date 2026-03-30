import { useNavigate } from "react-router-dom";
import usePageTitle from "../hooks/usePageTitle";
import { useState } from "react";
import axios from "axios";
import axiosInstance from "../api/AxiosConfig";
import AuthField from "../components/auth/AuthField";
import AuthRedirect from "../components/auth/AuthRedirect";

type RegisterErrors = {
    fullName?: string,
    email?: string,
    password?: string,
    confirmPassword?: string,
    form?: string
}

export default function RegisterPage() {
    usePageTitle("MiniTrello - Register")
    const navigate = useNavigate()
    const [fullName, setFullName] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [errors, setErrors] = useState<RegisterErrors>({})

    async function handleSubmit(e: React.SubmitEvent) {
        e.preventDefault()

        try {
            await axiosInstance.post("/api/auth/register", { fullName, email, password, confirmPassword })

            navigate("/")
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const message = error.response?.data.message;
                const field = error.response?.data.field;

                if (field)
                    setErrors({[field]: message})
                else
                    setErrors({form: "Something went wrong. Please try again later."})
            }
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
                            errorMessage={errors.fullName}
                        />
                        <AuthField
                            fieldLabel="Email"
                            fieldType="email"
                            fieldPlaceholder="your@email.com"
                            value={email}
                            onChange={setEmail}
                            errorMessage={errors.email}
                        />
                    </div>
                    <AuthField
                        fieldLabel="Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={password}
                        onChange={setPassword}
                        errorMessage={errors.password}
                    />
                    <AuthField
                        fieldLabel="Confirm Password"
                        fieldType="password"
                        fieldPlaceholder="••••••••"
                        value={confirmPassword}
                        onChange={setConfirmPassword}
                        errorMessage={errors.confirmPassword}
                    />
                    {errors.form && <p className="auth-error-form">{errors.form}</p>}
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