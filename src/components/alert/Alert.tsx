import React, {
  useState,
  useImperativeHandle,
  forwardRef,
  useEffect,
} from "react";
import "./Alert.scss";

type AlertType = "success" | "error" | "info" | "warning";

interface ParamsAlert {
  message: string;
  type?: AlertType;
  duration?: number;
  position?:
    | "top-left"
    | "top-right"
    | "bottom-left"
    | "bottom-right"
    | "center";
  customStyles?: React.CSSProperties;
  icon?: React.ReactNode;
}

export type AlertRef = {
  show: (params: ParamsAlert) => void;
  hide: () => void;
};

const Alert = forwardRef<AlertRef>((_, ref) => {
  const [isVisible, setIsVisible] = useState(false);
  const [alertParams, setAlertParams] = useState<ParamsAlert>({
    message: "",
    type: "info",
    position: "top-right",
  });

  const [shouldRender, setShouldRender] = useState(false); // Quản lý render
  const [progress, setProgress] = useState(0); // Tiến trình thanh progress

  useImperativeHandle(ref, () => ({
    show: ({
      message,
      type = "info",
      duration = 5000,
      position = "top-right",
      customStyles,
      icon,
    }: ParamsAlert) => {
      setAlertParams({ message, type, position, customStyles, icon });
      setShouldRender(true); // Hiển thị Alert
      setTimeout(() => setIsVisible(true), 10); // Bắt đầu animation

      setProgress(100); // Thanh progress bắt đầu đầy

      if (duration > 0) {
        const interval = duration / 100; // Mỗi phần trăm tương ứng với bao nhiêu ms
        let currentProgress = 100;

        const progressTimer = setInterval(() => {
          currentProgress -= 1;
          setProgress(currentProgress);

          if (currentProgress <= 0) {
            clearInterval(progressTimer);
            hide();
          }
        }, interval);
      }
    },
    hide: () => hide(),
  }));

  const hide = () => {
    setIsVisible(false); // Ẩn Alert
    setTimeout(() => setShouldRender(false), 300); // Đợi animation kết thúc
  };

  if (!shouldRender) return null;

  return (
    <div
      className={`alert ${alertParams.type} ${alertParams.position}`}
      style={{
        ...alertParams.customStyles,
        opacity: isVisible ? 1 : 0,
        transform: isVisible ? "translateY(0)" : "translateY(-20px)",
        transition: "opacity 0.3s ease, transform 0.3s ease",
      }}
    >
      <div className="alert-content">
        {alertParams.icon && <span className="icon">{alertParams.icon}</span>}
        <span className="message">{alertParams.message}</span>
        <button className="close-btn" onClick={hide}>
          &times;
        </button>
      </div>
      {/* Thanh progress */}
      <div
        className="progress-bar"
        style={{ width: `${progress}%`, transition: "width 0.1s linear" }}
      />
    </div>
  );
});

export default Alert;
