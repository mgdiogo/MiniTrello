import { useLayoutEffect } from "react"
import { Link } from "react-router-dom"

export default function HomePage() {
    useLayoutEffect(() => {
        document.title = "MiniTrello - Start Organizing Yourself!"
    }, [])

    return (
        <main>
            <div className="login-page">
                <div className="login-container">
                    <form>
                        <h1>Login</h1>
                        <div className="form-group">
                            <input className="form-input" type="email" placeholder="Email" required />
                        </div>
                        <div className="form-group">
                            <input className="form-input" type="password" placeholder="Password" required />
                        </div>
                    </form>
                    <div className="forgot-password">
                        <Link className="a" to="#">
                            Forgot Password?
                        </Link>
                    </div>
                    <div className="button-submit">
                        <button className="button-primary"type="submit">
                            Login
                        </button>
                    </div>
                </div>
            </div>
        </main>
    )
}