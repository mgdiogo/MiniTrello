import { Outlet } from "react-router-dom";
import logo from "../assets/logo-only.svg"
import "../styles/Auth.css"

export default function AuthLayout() {
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
                            <img src={logo} alt="MiniTrello Logo" className="auth-brand-title-logo" />
                            MiniTrello
                        </h1>
                        <p className="auth-brand-subtitle">Elevate your productivity flow.</p>
                    </div>
                    {/* Card */}
                    <Outlet />
                </main>
                <div className="auth-side-shape">
                    <div className="auth-side-shape-inner" />
                </div>
            </div>
        </>
    )
}