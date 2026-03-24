import { Link } from "react-router-dom"

type AuthRedirectProps = {
    footerText: string,
    footerAction: string,
    footerLink: string
}

export default function AuthRedirect({
    footerText,
    footerAction,
    footerLink
}: AuthRedirectProps) {
    return (
        <>
            <p className="auth-redirect">
                {footerText}
                <Link to={footerLink}>{footerAction}</Link>
            </p>
        </>
    )
}