import axios from "axios"

// Creates a custom Axios instance with a shared API config
// Requests automatically include auth cookies via withCredentials
const axiosInstance = axios.create()

axiosInstance.defaults.baseURL = import.meta.env.VITE_API_URL
axiosInstance.defaults.withCredentials = true
axiosInstance.defaults.headers.common["Content-Type"] = "application/json"

let refreshTokenRequest: Promise<void> | null = null

axiosInstance.interceptors.response.use(
	response => response,
	async error => {
		const originalRequest = error.config

		if (!originalRequest)
			return Promise.reject(error)

		const isAuthRoute =
			originalRequest.url === "/auth/refresh" ||
			originalRequest.url === "/auth/login" ||
			originalRequest.url === "/auth/register" ||
			originalRequest.url === "/auth/logout"

		if (isAuthRoute)
			return Promise.reject(error)

		// Retry the original request if it failed with a 401 Unauthorized error
		// This way if the access token has expired, an attempt will be made to refresh it instead of immediately logging the user out
		if (error.response?.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true

			try {
				if (!refreshTokenRequest) {
					refreshTokenRequest = axiosInstance.post("/auth/refresh")
						.then(() => { })
						.finally(() => {
							refreshTokenRequest = null
						})
				}

				await refreshTokenRequest
				return axiosInstance(originalRequest)
			} catch (error) {
				return Promise.reject(error)
			}
		}

		return Promise.reject(error)
	}
)

export default axiosInstance