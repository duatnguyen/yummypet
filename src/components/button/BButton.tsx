import React from "react";
import "./BButton.scss";
interface ButtonProps {
  type?: "button" | "submit" | "reset"; // Loại button
  onClick?: () => void;
  disabled?: boolean; // Trạng thái vô hiệu hóa
  className?: string; // Thêm custom class nếu cần
  style?: React.CSSProperties;
  label?: string; // Văn bản hiển thị trên button
  variant?: "primary" | "secondary" | "danger"; // Kiểu dáng button
}

const BButton = (props: ButtonProps) => {
  const {
    type,
    onClick,
    disabled ,
    className ,
    style,
    label ,
    variant ,
  } = props;
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`button button-${variant} ${className}`}
      style={style}
    >
      {label}
    </button>
  );
};

export default BButton;
