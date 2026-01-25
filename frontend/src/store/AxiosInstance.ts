import axios from "axios";

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8098',
    withCredentials: true
})

export default axiosInstance;