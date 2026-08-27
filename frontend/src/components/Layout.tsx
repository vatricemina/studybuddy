import type { ReactNode } from 'react';
import Navbar from './Navbar';

function Layout({ children }: { children: ReactNode }) {
    return (
        <div className="min-h-screen bg-neutral-950">
            <Navbar />
            <div className="max-w-6xl mx-auto p-6">
                {children}
            </div>
        </div>
    );
}

export default Layout;