import { Link, Outlet } from "react-router-dom";
import { BiAddToQueue } from "react-icons/bi";
import usePrivateClientRequest from "../hooks/usePrivateClientRequest";
import { useCallback, useEffect, useState } from "react";
import ChatRoomListItem from "../components/ChatRoomListItem";

const ChatRoomsLayout = () => {

  const [chatRoomListItems, setChatRoomListItems] = useState([]);

  const privateClientRequest = usePrivateClientRequest();

  useEffect(() => {
    let isMounted = true;
    const abortController = new AbortController();

    const cleanup = () => {
      isMounted = false;
      abortController.abort();
    };

    const fetchChatRoomListItems = async () => {
      try {
        const response = await privateClientRequest.get(
          "/api/chatroom",
          {
            signal: abortController.signal,
          }
        );
        if (isMounted) {
          setChatRoomListItems(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchChatRoomListItems();
    return cleanup;
  }, []);

  const renderedChatRoomListItems = chatRoomListItems.map(
    (chatRoom, index) => (
      <ChatRoomListItem 
        key={index}
        chatRoomId={chatRoom.chatRoomId}
        name={chatRoom.name}
      />
    )
  );

  return (
    <div className="flex gap-4 max-w-[90rem] min-h-screen bg-gray-400">
      <div className="w-full max-w-lg bg-red-200">
        <h1>There will be chat rooms</h1>
        <Link to={"create"} className="flex gap-4 bg-white items-center p-4 rounded">
          Create chatroom
          <BiAddToQueue size={20} />
        </Link>
        <div>
          {renderedChatRoomListItems}
        </div>
      </div>
      <div className="w-full">
        <Outlet />
      </div>
    </div>
  );
};

export default ChatRoomsLayout;
