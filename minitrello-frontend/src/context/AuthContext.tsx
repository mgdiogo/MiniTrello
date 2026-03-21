import { createContext, useState, type ReactNode } from "react"

// This interface defines the user object that is return on login to match LoginReponse in the backend
interface User {
    userId: number,
    email: string,
    token: string
}

// This interface defines everything exposed to the rest of the app
// Any component calling useAuth() will have access to these properties and functions
interface AuthContextType {
    user: User | null,
    isAuthenticated: boolean,
    login: (data: User) => void,
    logout: () => void
}

export const AuthContext = createContext<AuthContextType | null>(null)

// AuthProvider holds the auth state and makes it available to every child component
// { children } represents everything nested inside <AuthProvider>...</AuthProvider>
export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(() => {
        const storedUser = localStorage.getItem("user")
        return storedUser ? JSON.parse(storedUser) : null
    })

    const login = (data: User) => {
        setUser(data)
        localStorage.setItem("user", JSON.stringify(data))
    }

    const logout = () => {
        setUser(null)
        localStorage.removeItem("user")
    }

    return (
        <AuthContext.Provider value={{
            user,
            isAuthenticated: !!user,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    )
}
