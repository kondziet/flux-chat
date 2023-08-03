import { Link } from "react-router-dom";

const SidebarIcon = ({ icon, navigateTo }) => {
  return (
    <Link to={navigateTo} className="cursor-pointer hover:bg-gray-200 flex justify-center items-center h-12 w-12 rounded bg-white">
      {icon}
    </Link>
  );
};

export default SidebarIcon;
