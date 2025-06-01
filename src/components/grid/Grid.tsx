import React from 'react';
import './Grid.scss';

interface GridProps {
  children: React.ReactNode;
  columns?: number | string; // Số cột (số hoặc phân tỷ lệ ví dụ "1fr 1fr")
  gap?: string; // Khoảng cách giữa các item
  responsive?: {
    small?: number; // Số cột khi màn hình nhỏ
    medium?: number; // Số cột khi màn hình trung bình
    large?: number; // Số cột khi màn hình lớn
  };
}

const Grid: React.FC<GridProps> = ({
  children,
  columns = 4,
  gap = '1rem',
  responsive = {},
}) => {
  const getGridTemplateColumns = () => {
    // Nếu có cấu hình responsive, sử dụng media query
    if (responsive) {
      const { small, medium, large } = responsive;
      return `
        grid-template-columns: repeat(${small ?? columns}, 1fr);
        @media (min-width: 576px) {
          grid-template-columns: repeat(${medium ?? columns}, 1fr);
        }
        @media (min-width: 768px) {
          grid-template-columns: repeat(${large ?? columns}, 1fr);
        }
      `;
    }

    // Nếu không có responsive, sử dụng cột mặc định
    return `grid-template-columns: repeat(${columns}, 1fr)`;
  };

  return (
    <div
      className="grid-container"
      style={{
        gap,
        gridTemplateColumns: getGridTemplateColumns(), // Sử dụng trực tiếp CSS string từ function
      }}
    >
      {children}
    </div>
  );
};


export default Grid;
