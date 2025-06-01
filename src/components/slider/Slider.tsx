// PriceRangeSlider.tsx
import React, { useState, useRef, useEffect, useCallback } from "react";
import "./Slider.scss";
import { debounce } from "lodash";
interface PriceRangeSliderProps {
  min: number;
  max: number;
  step?: number;
  defaultMinValue?: number;
  defaultMaxValue?: number;
  onChange?: (minValue: number, maxValue: number) => void;
  formatValue?: (value: number) => string;
}

const PriceRangeSlider: React.FC<PriceRangeSliderProps> = ({
  min,
  max,
  step = 1,
  defaultMinValue = min,
  defaultMaxValue = max,
  onChange,
  formatValue = (value) => `${value.toLocaleString()}đ`,
}) => {
  console.log("render Slider");
  const [minValue, setMinValue] = useState<number>(defaultMinValue);
  const [maxValue, setMaxValue] = useState<number>(defaultMaxValue);
  const [isDragging, setIsDragging] = useState<"min" | "max" | null>(null);
  const [displayMin, setDisplayMin] = useState<number>(defaultMinValue);
  const [displayMax, setDisplayMax] = useState<number>(defaultMaxValue);

  const sliderRef = useRef<HTMLDivElement>(null);
  const minThumbRef = useRef<HTMLDivElement>(null);
  const maxThumbRef = useRef<HTMLDivElement>(null);
  const animationFrameRef = useRef<number>();

  // Debounced onChange handler
  const debouncedApiCall = useCallback(
    debounce((min: number, max: number) => {
      onChange?.(min, max);
    }, 300), // Gọi API sau 300ms không thao tác
    [onChange]
  );

  // Calculate percentage for positioning
  const getPercentage = useCallback(
    (value: number) => ((value - min) / (max - min)) * 100,
    [min, max]
  );

  // Convert position to value
  const getValueFromPosition = useCallback(
    (position: number, width: number) => {
      const percentage = Math.max(0, Math.min(position / width, 1));
      const rawValue = percentage * (max - min) + min;
      const steppedValue = Math.round(rawValue / step) * step;
      return Math.max(min, Math.min(max, steppedValue));
    },
    [min, max, step]
  );

  // Smooth value updates
  useEffect(() => {
    const animateValue = (
      currentValue: number,
      targetValue: number,
      setValue: (value: number) => void
    ) => {
      if (Math.abs(currentValue - targetValue) < step / 2) {
        setValue(targetValue);
        return;
      }

      const newValue = currentValue + (targetValue - currentValue) * 0.2;
      setValue(newValue);
      requestAnimationFrame(() =>
        animateValue(newValue, targetValue, setValue)
      );
    };

    if (!isDragging) {
      animateValue(displayMin, minValue, setDisplayMin);
      animateValue(displayMax, maxValue, setDisplayMax);
    }
  }, [minValue, maxValue, isDragging, step]);

  // Handle mouse/touch movement
  const handleMove = useCallback(
    (clientX: number) => {
      if (!sliderRef.current || !isDragging) return;

      const { left, width } = sliderRef.current.getBoundingClientRect();
      const position = clientX - left;
      const newValue = getValueFromPosition(position, width);

      if (isDragging === "min") {
        const clampedValue = Math.min(newValue, maxValue - step);
        setMinValue(clampedValue);
        setDisplayMin(clampedValue);
        debouncedApiCall(clampedValue, maxValue); // Chỉ gọi API khi dừng
      } else {
        const clampedValue = Math.max(newValue, minValue + step);
        setMaxValue(clampedValue);
        setDisplayMax(clampedValue);
        debouncedApiCall(minValue, clampedValue); // Chỉ gọi API khi dừng
      }
    },
    [
      isDragging,
      maxValue,
      minValue,
      step,
      getValueFromPosition,
      debouncedApiCall,
    ]
  );

  // Mouse event handlers
  const handleMouseMove = useCallback(
    (e: MouseEvent) => {
      e.preventDefault();
      handleMove(e.clientX);
    },
    [handleMove]
  );

  const handleTouchMove = useCallback(
    (e: TouchEvent) => {
      e.preventDefault();
      handleMove(e.touches[0].clientX);
    },
    [handleMove]
  );

  const handleDragEnd = useCallback(() => {
    setIsDragging(null);
  }, []);
  const handleSliderClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (!sliderRef.current) return;
  
    const { left, width } = sliderRef.current.getBoundingClientRect();
    const position = e.clientX - left;
    const clickedValue = getValueFromPosition(position, width);
  
    // Xác định giá trị gần hơn để cập nhật
    const distanceToMin = Math.abs(clickedValue - minValue);
    const distanceToMax = Math.abs(clickedValue - maxValue);
  
    if (distanceToMin < distanceToMax && clickedValue <= maxValue - step) {
      setMinValue(clickedValue);
      setDisplayMin(clickedValue);
      debouncedApiCall(clickedValue, maxValue);
    } else if (clickedValue >= minValue + step) {
      setMaxValue(clickedValue);
      setDisplayMax(clickedValue);
      debouncedApiCall(minValue, clickedValue);
    }
  };
  
  // Set up event listeners
  useEffect(() => {
    if (isDragging) {
      window.addEventListener("mousemove", handleMouseMove, { passive: false });
      window.addEventListener("touchmove", handleTouchMove, { passive: false });
      window.addEventListener("mouseup", handleDragEnd);
      window.addEventListener("touchend", handleDragEnd);

      return () => {
        window.removeEventListener("mousemove", handleMouseMove);
        window.removeEventListener("touchmove", handleTouchMove);
        window.removeEventListener("mouseup", handleDragEnd);
        window.removeEventListener("touchend", handleDragEnd);
      };
    }
  }, [isDragging, handleMouseMove, handleTouchMove, handleDragEnd]);

  return (
    <div className="price-range-slider">
      <div className="price-range-values">
        <span>{formatValue(Math.round(displayMin))}</span>
        <span>{formatValue(Math.round(displayMax))}</span>
      </div>

      <div className="slider-container" ref={sliderRef} onClick={handleSliderClick}>
        <div className="slider-track" />

        <div
          className="slider-range"
          style={{
            left: `${getPercentage(displayMin)}%`,
            width: `${getPercentage(displayMax) - getPercentage(displayMin)}%`,
          }}
        />

        <div
          ref={minThumbRef}
          className={`slider-thumb ${isDragging === "min" ? "dragging" : ""}`}
          style={{ left: `${getPercentage(displayMin)}%` }}
          onMouseDown={() => setIsDragging("min")}
          onTouchStart={() => setIsDragging("min")}
        />

        <div
          ref={maxThumbRef}
          className={`slider-thumb ${isDragging === "max" ? "dragging" : ""}`}
          style={{ left: `${getPercentage(displayMax)}%` }}
          onMouseDown={() => setIsDragging("max")}
          onTouchStart={() => setIsDragging("max")}
        />
      </div>
    </div>
  );
};

export default PriceRangeSlider;
