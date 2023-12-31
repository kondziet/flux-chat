import React, { useState, useCallback, useEffect } from "react";
import useWebSocket, { ReadyState } from "react-use-websocket";

const WebSocketClient = ({ chatRoomId }) => {
  const [socketUrl, setSocketUrl] = useState(`ws://localhost:8080/chat/${chatRoomId}`);
  const [messageHistory, setMessageHistory] = useState([]);

  const [messageInput, setMessageInput] = useState("");

  const { sendMessage, lastMessage, readyState } = useWebSocket(socketUrl);

  useEffect(() => {
    if (lastMessage !== null) {
      setMessageHistory((prev) => prev.concat(lastMessage));
    }
  }, [lastMessage, setMessageHistory]);

  const handleClickSendMessage = useCallback(() =>
    sendMessage(
      JSON.stringify({
        content: messageInput
      })
    )
  );

  const connectionStatus = {
    [ReadyState.CONNECTING]: "Connecting",
    [ReadyState.OPEN]: "Open",
    [ReadyState.CLOSING]: "Closing",
    [ReadyState.CLOSED]: "Closed",
    [ReadyState.UNINSTANTIATED]: "Uninstantiated",
  }[readyState];

  return (
    <div>
      <input
        value={messageInput}
        onChange={(event) => setMessageInput(event.target.value)}
      />
      <button
        onClick={handleClickSendMessage}
        disabled={readyState !== ReadyState.OPEN}
      >
        Send
      </button>
      <span>The WebSocket is currently {connectionStatus}</span>

      <ul>
        {messageHistory.map((message, idx) => (
          <span key={idx}>{message ? message.data : null}</span>
        ))}
      </ul>
    </div>
  );
};

export default WebSocketClient;
