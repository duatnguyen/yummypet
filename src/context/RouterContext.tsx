import React, { ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import { _router } from "./routerSingleton";

interface CustomRouterProviderProps {
  children: ReactNode;
}

export const CustomRouterProvider: React.FC<CustomRouterProviderProps> = ({ children }) => {
  const navigate = useNavigate();

  // Gán navigate vào singleton
  _router.setNavigate(navigate);

  return <>{children}</>;
};
