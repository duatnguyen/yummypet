import React from "react";
import { AlertRef } from "./components/alert/Alert";

export const _global = {
  event: {
    // loading: React.createRef<SDKLoadingRef>(),
    alert: React.createRef<AlertRef>(),
  },
};
