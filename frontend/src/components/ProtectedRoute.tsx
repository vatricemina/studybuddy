import { Navigate } from 'react-router-dom';
import type { ReactNode } from 'react';

function ProtectedRoute({ children }: {children: ReactNode}){
    const token=localStorage.getItem("token");
    if(!token){
        return <Navigate to="/login"/>
    }

    return <>{children}</>
}

export default ProtectedRoute;