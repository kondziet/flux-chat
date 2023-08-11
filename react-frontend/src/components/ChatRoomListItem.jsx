import { Link } from "react-router-dom";

const ChatRoomListItem = ({ chatRoomId, name }) => {

    return (
        <Link to={chatRoomId} className="h-full w-full flex flex-col items-center justify-between bg-blue-300 rounded-lg shadow p-2">
            <p>{name}</p>
        </Link>
    );
}

export default ChatRoomListItem;