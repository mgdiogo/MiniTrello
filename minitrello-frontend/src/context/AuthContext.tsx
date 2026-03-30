import { createContext, useEffect, useState, type ReactNode } from "react"
import axiosInstance from "../api/AxiosConfig"
import axios from "axios"

// This interface defines the user object that is return on login to match LoginReponse in the backend
interface User {
    userId: number,
    email: string,
}

// This interface defines everything exposed to the rest of the app
// Any component calling useAuth() will have access to these properties and functions
interface AuthContextType {
    user: User | null,
    isAuthenticated: boolean,
    isLoading: boolean,
    login: (data: User) => void,
    logout: () => Promise<void>,
    checkUserState: () => Promise<void>
}

export const AuthContext = createContext<AuthContextType | null>(null)

// AuthProvider holds the auth state and makes it available to every child component
// { children } represents everything nested inside <AuthProvider>...</AuthProvider>
export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null)
    const [isLoading, setLoading] = useState(true)

    // Check if the user is authenticated on app load
    // This allows the app to maintain the user session across page refreshes/app restarts as long as the auth cookie is valid
    const checkUserState = async () => {
        setLoading(true)

        try {
            const response = await axiosInstance.get<User | null>("/auth/me")
            setUser(response.data)
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status !== 401)
                console.error("Error occurred while checking user state:", error)
            setUser(null)
        } finally {
            setLoading(false)
        }
    }

    // On login we update the user state with the data returned from the backend {userId, email}
    // So it can be later accessed from any component that calls useAuth()
    const login = (data: User) => {
        setUser(data)
    }

    const logout = async () => {
        setLoading(true)

        try {
            await axiosInstance.post("/auth/logout")
        } catch (error) {
            console.error("Error occurred while logging out:", error)
        } finally {
            setUser(null)
            setLoading(false)
        }
    }

    // useEffect makes sure we check the user state as soon as the AuthProvider is mounted (app load)
    useEffect(() => {
        checkUserState()
    }, [])

    return (
        <AuthContext.Provider value={{
            user,
            isAuthenticated: !!user,
            isLoading,
            login,
            logout,
            checkUserState
        }}>
            {children}
        </AuthContext.Provider>
    )
}
