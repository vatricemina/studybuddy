import type { ReactNode } from 'react';
import Navbar from './Navbar';

function Layout({ children }: { children: ReactNode }) {
    return (
        <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-rose-50 to-purple-100">            <Navbar />
            <div className="max-w-5xl mx-auto p-6">
                {children}
            </div>
        </div>
    );
}

export default Layout;