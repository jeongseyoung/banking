import axios, { AxiosResponse } from "axios";
import axiosInstance from "../store/AxiosInstance";
import ApiConfig from "./ApiConfig";

type HttpMethod = "GET" | "POST" | "DELETE" | "UPDATE";

export const api = async <TResponse = any> (
    url: string, 
    method: HttpMethod = "GET", 
    data?: any,
    config?: ApiConfig
): Promise<TResponse> => {
    console.log("api", config?.params)
    switch (method) {

        case "GET": {
            const response = await axiosInstance.get<TResponse>(url, {params: config?.params});
            return response.data
        };

        case "POST": {
            const response = await axiosInstance.post<TResponse>(url, data);
            return response.data
        };

        case "DELETE": {
            const response = await axios.delete<TResponse>(url, {params: config?.params});
            return response.data;
        }

        default:
            throw new Error(`지원하지 않는 HTTP 메서드: ${method}`);
        }    

};