import React from "react";
import { RouterProvider } from "react-router-dom";
import { router } from "./routers/router";
import Alert from "./components/alert/Alert";
import { _global } from "./global";
import { Provider } from "react-redux";
import store from "./redux/store/store";

const App: React.FC = () => {
  return (
    <Provider store={store}>
      <Alert ref={_global.event.alert} />

      <RouterProvider router={router} />
    </Provider>
  );
};

export default App;
