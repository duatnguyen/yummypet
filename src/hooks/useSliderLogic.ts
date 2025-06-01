// hooks/useSliderLogic.ts
import { useCallback, useMemo, useState } from "react";

interface UseSliderLogicProps {
  min: number;
  max: number;
  step: number;
  defaultMinValue: number;
  defaultMaxValue: number;
}

export function useSliderLogic({
  min,
  max,
  step,
  defaultMinValue,
  defaultMaxValue,
}: UseSliderLogicProps) {
  const [minValue, setMinValue] = useState(defaultMinValue);
  const [maxValue, setMaxValue] = useState(defaultMaxValue);
  const [isDragging, setIsDragging] = useState<"min" | "max" | null>(null);

  const getPercentage = useCallback(
    (value: number) => {
      return ((value - min) / (max - min)) * 100;
    },
    [min, max]
  );

  const getValueFromPosition = useCallback(
    (position: number, width: number) => {
      const percentage = Math.max(0, Math.min(position / width, 1));
      const rawValue = percentage * (max - min) + min;
      return Math.round(rawValue / step) * step;
    },
    [min, max, step]
  );

  const updateValue = useCallback(
    (newValue: number, type: "min" | "max") => {
      if (type === "min") {
        setMinValue(Math.min(newValue, maxValue - step));
      } else {
        setMaxValue(Math.max(newValue, minValue + step));
      }
    },
    [maxValue, minValue, step]
  );

  return {
    minValue,
    maxValue,
    isDragging,
    setIsDragging,
    getPercentage,
    getValueFromPosition,
    updateValue,
  };
}
