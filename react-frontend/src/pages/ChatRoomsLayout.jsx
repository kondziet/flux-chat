import { Outlet } from "react-router-dom";
import { BiAddToQueue } from "react-icons/bi";
import usePrivateClientRequest from "../hooks/usePrivateClientRequest";

const ChatRoomsLayout = () => {

  const privateClientRequest = usePrivateClientRequest();

  const handleClick = async () => {
    const response = await privateClientRequest.post("/api/chatroom");
    console.log(response);
  };

  return (
    <div className="flex gap-4 max-w-[90rem] min-h-screen bg-gray-400">
      <div className="w-full max-w-lg bg-red-200">
        <h1>There will be chat rooms</h1>
        <button onClick={handleClick} className="flex gap-4 bg-white items-center p-4 rounded">
          Create chatroom
          <BiAddToQueue size={20} />
        </button>
      </div>
      <div className="w-full">
        <Outlet />
      </div>
    </div>
  );
};

export default ChatRoomsLayout;
