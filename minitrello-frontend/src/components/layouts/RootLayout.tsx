import { Outlet } from 'react-router-dom'

export default function RootLayout() {
    return (
        <div className='root-layout'>
            <header>
                <nav>
                    { /* TODO: Add navigation links */ }
                </nav>
            </header>
            <main>
                <Outlet /> { /* This is where the child routes will be rendered */ }
            </main>
            <footer>
                <p className='footer-text'>MiniTrello</p>
            </footer>
        </div>
    )
}
