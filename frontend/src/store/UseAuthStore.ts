import { create } from "zustand";
import { AuthState } from "../types/AuthState";
import axios from "axios";
import { UserResponse } from "../types/UserResponse";
import axiosInstance from "./AxiosInstance";

export const useAuthStore = create<AuthState>((set) => ({
    user: null,
    isAuthenticated: false, 
    isLoading: true,
    checkAuth: async () => {
        try {
            const res = await axiosInstance.get<UserResponse>("/api/auth/me", {
                withCredentials: true //쿠키 백엔드로 전송
            });
            set({
                user: res.data,
                isAuthenticated: true
            });
        } catch (error) {
            console.log("error", error);
            set({
                user: null,
                isAuthenticated: false
            })
        } finally {
            set({ isLoading: false });
        }
    }, 
    logout: async () => {
        await axios.post("/auth/logout", {}, {withCredentials: true });
        set({
            user:null,
            isAuthenticated: false
        })
    }
}));