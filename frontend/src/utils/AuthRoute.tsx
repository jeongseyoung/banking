import { Navigate } from "react-router-dom";
import { useAuthStore } from "../store/UseAuthStore";

export function AuthenticatedRoute({children}: {children: JSX.Element}) {

    const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
    const isLoading = useAuthStore((state) => state.isLoading);

    console.log("isauthenticated", isAuthenticated, isLoading);
    
    if (isLoading) return null;

    if (!isAuthenticated) {
        return <Navigate to="/" replace />;
    }

    return children;
}