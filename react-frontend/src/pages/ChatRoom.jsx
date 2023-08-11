import { useParams } from "react-router-dom";
import WebSocketClient from "../components/WebSocketClient";

const ChatRoom = () => {

  const { chatRoomId } = useParams();

  return (
    <div>
      <h1>chat room {chatRoomId}</h1>
      <WebSocketClient chatRoomId={chatRoomId} />
    </div>
  );
};

export default ChatRoom;
