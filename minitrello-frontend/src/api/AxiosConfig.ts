import axios from "axios"

// Creates a custom Axios instance with a base config
// Every request made with this instance will automatically include the token
const axiosInstance = axios.create()

// Interceptors run before every request is sent
// Here we grab the token from localStorage and attach it to the headers
axiosInstance.interceptors.request.use((config) => {
  const stored = localStorage.getItem("user")
  const user = stored ? JSON.parse(stored) : null

  if (user?.token)
    config.headers.Authorization = `Bearer ${user.token}`

  return config
})

export default axiosInstance