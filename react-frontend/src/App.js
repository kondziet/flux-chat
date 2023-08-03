import { BrowserRouter, Route, Routes } from "react-router-dom";
import WebSocketClient from "./components/WebSocketClient";
import { AuthenticationProvider } from "./context/AuthenticationContext";
import AuthenticationPage from "./pages/AuthenticationPage";
import HomePage from "./pages/HomePage";
import NotFoundPage from "./pages/NotFoundPage";
import ChatRoomsLayout from "./pages/ChatRoomsLayout";
import DefaultChatRoom from "./pages/DefaultChatRoom";
import ChatRoom from "./pages/ChatRoom";
import SidebarLayout from "./pages/SidebarLayout";
import FriendsPage from "./pages/FriendsPage";

const App = () => {
  return (
    <div className="h-screen">
      <BrowserRouter>
        <AuthenticationProvider>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/authentication" element={<AuthenticationPage />} />
            <Route path="/sidebar" element={<SidebarLayout />}>
              <Route path="chatroom" element={<ChatRoomsLayout />}>
                <Route index element={<DefaultChatRoom />} />
                <Route path=":chatRoomId" element={<ChatRoom />} />
              </Route>
              <Route path="friends" element={<FriendsPage />} />
            </Route>
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
          {/* <WebSocketClient /> */}
        </AuthenticationProvider>
      </BrowserRouter>
    </div>
  );
};

export default App;
