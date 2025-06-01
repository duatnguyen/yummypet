import React from "react";
import classNames from "classnames";
import "./Pagination.scss";

type PaginationProps = {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  siblingCount?: number; // số trang hiển thị hai bên trang hiện tại (mặc định là 1)
  className?: string; // cho phép thêm className tùy chỉnh
};

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  siblingCount = 1,
  className,
}) => {
  const getPaginationRange = () => {
    const totalPageNumbers = siblingCount * 2 + 5; // 5 = current + prev + next + 2 dots

    if (totalPages <= totalPageNumbers) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    const startPage = Math.max(2, currentPage - siblingCount);
    const endPage = Math.min(totalPages - 1, currentPage + siblingCount);

    const hasLeftDots = startPage > 2;
    const hasRightDots = endPage < totalPages - 1;

    const pages: (number | "...")[] = [1];

    if (hasLeftDots) pages.push("...");

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    if (hasRightDots) pages.push("...");

    pages.push(totalPages);

    return pages;
  };

  const handlePageChange = (page: number | "...") => {
    if (page !== "..." && page !== currentPage) {
      onPageChange(page);
    }
  };

  const paginationRange = getPaginationRange();

  return (
    <nav className={classNames("pagination", className)} aria-label="Pagination">
      <button
        className="pagination__button"
        onClick={() => handlePageChange(currentPage - 1)}
        disabled={currentPage === 1}
      >
        &laquo;
      </button>
      {paginationRange.map((page, index) => (
        <button
          key={index}
          className={classNames("pagination__item", {
            "pagination__item--active": page === currentPage,
            "pagination__item--dots": page === "...",
          })}
          onClick={() => handlePageChange(page)}
          disabled={page === "..."}
        >
          {page}
        </button>
      ))}
      <button
        className="pagination__button"
        onClick={() => handlePageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
      >
        &raquo;
      </button>
    </nav>
  );
};

export default Pagination;