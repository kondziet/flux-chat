import { BsChatDots } from "react-icons/bs";
import { AiOutlineLogout } from "react-icons/ai";
import { LiaUserFriendsSolid } from "react-icons/lia";
import SidebarIcon from "./SidebarIcon";

const Sidebar = () => {
  return (
    <div className="h-full w-full flex flex-col items-center justify-between bg-orange-300">
      <div className="">
        <SidebarIcon navigateTo={"chatroom"} icon={<BsChatDots size={30} />} />
        <SidebarIcon navigateTo={"friends"} icon={<LiaUserFriendsSolid size={30} />} />
      </div>
      <div>
        <SidebarIcon icon={<AiOutlineLogout size={30} />} />
      </div>
    </div>
  );
};

export default Sidebar;
