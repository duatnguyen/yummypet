import React, { useEffect } from "react";
import { Outlet, useLocation } from "react-router-dom";
import images from "../res/images";
import "./Layout.scss"

const Layout = () => {
  console.log('render', );
  const location = useLocation(); 

  useEffect(() => {
    window.scrollTo(0, 0); 
  }, [location]); 
  return (
    <div >

      <header className="header-container">
        <div >
          <img src={images.logo} className="logo" />
        </div>
        <h1 >Hệ thống bán hàng</h1>
        <h1 >Hệ thống bán hàng</h1>

      </header>


      <main >
        <Outlet />
      </main>


      <footer>
        <p>&copy; 2024 Hệ thống bán hàng. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Layout;
