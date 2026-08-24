import type { ReactNode } from 'react';
import Navbar from './Navbar';

function Layout({children}:{children:ReactNode}){
    return (
        <div>
            <Navbar />
            <div style={{ padding: "20px" }}>
                {children}
            </div>
        </div>
    );
}
export default Layout;