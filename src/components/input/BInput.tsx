import React from 'react';
import "./BInput.scss"
interface InputProps {
  type?: | "button"
  | "checkbox"
  | "color"
  | "date"
  | "datetime-local"
  | "email"
  | "file"
  | "hidden"
  | "image"
  | "month"
  | "number"
  | "password"
  | "radio"
  | "range"
  | "reset"
  | "search"
  | "submit"
  | "tel"
  | "text"
  | "time"
  | "url"
  | "week"; // Loại input: text, email, password, v.v.
  value?: string;
  placeholder?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur?: (e: React.FocusEvent<HTMLInputElement>) => void;
  onFocus?: (e: React.FocusEvent<HTMLInputElement>) => void;
  disabled?: boolean;
  error?: string; // Hiển thị thông báo lỗi nếu có
  className?: string; // Thêm custom class nếu cần
  style?: React.CSSProperties;
  label?: string; // Nhãn cho input
  required?: boolean; // Dấu * bắt buộc
}

const BInput: React.FC<InputProps> = ({
  type = "text",
  value = "",
  placeholder = "",
  onChange,
  onBlur,
  onFocus,
  disabled = false,
  error,
  className = "",
  style,
  label,
  required = false,
}) => {
  return (
    <div className={`input-wrapper ${className}`} style={style}>
      {label && (
        <label className="input-label">
          {label} {required && <span className="required">*</span>}
        </label>
      )}
      <input
        type={type}
        value={value}
        placeholder={placeholder}
        onChange={onChange}
        onBlur={onBlur}
        onFocus={onFocus}
        disabled={disabled}
        className={`input-field ${error ? "input-error" : ""}`}
      />
      {error && <p className="input-error-message">{error}</p>}
    </div>
  );
};

export default BInput;