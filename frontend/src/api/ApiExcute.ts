import { api } from "./api";

export const apiGet = <TResponse = any> (url: string, params?: Record<string,any>): Promise<TResponse> => api<TResponse>(url, "GET", {params});
export const apiPost = <TResponse = any> (url: string, data?: any): Promise<TResponse> => api<TResponse>(url, "POST", data);