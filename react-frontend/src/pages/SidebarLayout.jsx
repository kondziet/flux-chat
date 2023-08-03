import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";

const SidebarLayout = () => {
  return (
    <div className="mx-auto flex min-h-screen w-full bg-green-300 max-w-7xl">
      <div className="w-full max-w-[4rem]">
        <Sidebar />
      </div>
      <div className="w-full">
        <Outlet />
      </div>
    </div>
  );
};

export default SidebarLayout;
