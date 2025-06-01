// types.ts
export interface MenuItem {
  id: number;
  label: string;
  href: string;
  icon?: string;
  children?: MenuItem[];
  disabled?: boolean;
  onClick?: () => void;
}

export interface DropdownStyles {
  menuWidth?: string;
  menuMaxHeight?: string;
  backgroundColor?: string;
  hoverBackgroundColor?: string;
  textColor?: string;
  fontSize?: string;
  borderRadius?: string;
  padding?: string;
  animation?: "fade" | "slide" | "none";
  position?: "left" | "right" | "center";
  submenuPosition?: "right" | "below";
}

export interface DropdownProps {
  label: string | React.ReactNode;
  items: MenuItem[];
  className?: string;
  styles?: DropdownStyles;
  openOnHover?: boolean;
  closeOnClick?: boolean;
  hoverDelay?: number;
  closeDelay?: number;
  showArrow?: boolean;
  customArrow?: React.ReactNode;
  disabled?: boolean;
  loading?: boolean;
  loadingSpinner?: React.ReactNode;
  onOpen?: () => void;
  onClose?: () => void;
  renderItem?: (item: MenuItem) => React.ReactNode;
}

// HeaderDropdown.tsx
import React, { useState, useRef, useEffect, useCallback } from "react";
// import { MenuItem, DropdownProps, DropdownStyles } from './types';
import "./MenuDrop.scss";



const MenuDrop: React.FC<DropdownProps> = ({
  label,
  items,
  className = "",
  styles = {},
  openOnHover = false,
  closeOnClick = true,
  hoverDelay = 200,
  closeDelay = 400,
  showArrow = true,
  customArrow,
  disabled = false,
  loading = false,
  loadingSpinner,
  onOpen,
  onClose,
  renderItem,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [activeSubmenu, setActiveSubmenu] = useState<number | null>(null);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const openTimeoutRef = useRef<NodeJS.Timeout>();
  const closeTimeoutRef = useRef<NodeJS.Timeout>();

  const handleOpen = useCallback(() => {
    if (disabled || loading) return;
    clearTimeout(closeTimeoutRef.current);
    openTimeoutRef.current = setTimeout(() => {
      setIsOpen(true);
      onOpen?.();
    }, hoverDelay);
  }, [disabled, loading, hoverDelay, onOpen]);

  const handleClose = useCallback(() => {
    clearTimeout(openTimeoutRef.current);
    closeTimeoutRef.current = setTimeout(() => {
      setIsOpen(false);
      setActiveSubmenu(null);
      onClose?.();
    }, closeDelay);
  }, [closeDelay, onClose]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        handleClose();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      clearTimeout(openTimeoutRef.current);
      clearTimeout(closeTimeoutRef.current);
    };
  }, [handleClose]);

  const generateStyles = () => {
    const {
      menuWidth = "200px",
      menuMaxHeight = "400px",
      backgroundColor = "white",
      hoverBackgroundColor = "#f5f5f5",
      textColor = "#333",
      fontSize = "16px",
      borderRadius = "4px",
      padding = "10px 16px",
      animation = "slide",
      position = "left",
      submenuPosition = "right",
    } = styles;

    return {
      "--menu-width": menuWidth,
      "--menu-max-height": menuMaxHeight,
      "--background-color": backgroundColor,
      "--hover-background-color": hoverBackgroundColor,
      "--text-color": textColor,
      "--font-size": fontSize,
      "--border-radius": borderRadius,
      "--padding": padding,
      "--animation": animation,
      "--position": position,
      "--submenu-position": submenuPosition,
    } as React.CSSProperties;
  };

  const handleItemClick = (item: MenuItem) => {
    if (item.disabled) return;
    item.onClick?.();
    if (closeOnClick && !item.children) {
      handleClose();
    }
  };

  const renderDropdownItem = (item: MenuItem) => {
    if (renderItem) {
      return renderItem(item);
    }

    return (
      <div className="dropdown-item-content">
        {item.icon && <span className="dropdown-item-icon">{item.icon}</span>}
        <span className="dropdown-item-label">{item.label}</span>
        {item.children && <span className="dropdown-item-arrow">›</span>}
      </div>
    );
  };
  // const renderDropdownItem = (item: MenuItem) => {
  //   return items.map(item => (
  //     <li key={item.id} className="dropdown-item">
  //       <a href={item.href} className="dropdown-link">
  //         {item.label}
  //       </a>
  //       {item.children && (
  //         <ul className="dropdown-submenu">
  //           {/* {renderDropdownItem(item)} */}
  //         </ul>
  //       )}
  //     </li>
  //   ));
  // };

  // return (
  //   <ul className="dropdown-list">
  //     {renderDropdownItem(items)}
  //   </ul>
  // );


  return (
    <div
      className={`dropdown ${className} ${disabled ? "disabled" : ""}`}
      ref={dropdownRef}
      style={generateStyles()}
      onMouseEnter={openOnHover ? handleOpen : undefined}
      onMouseLeave={openOnHover ? handleClose : undefined}
    >
      <button
        className="dropdown-button"
        onClick={openOnHover ? undefined : () => setIsOpen(!isOpen)}
        disabled={disabled || loading}
      >
        {loading ? (
          loadingSpinner || <span className="loading-spinner" />
        ) : (
          <>
            {label}
            {showArrow &&
              (customArrow || (
                <span className={`arrow ${isOpen ? "arrow-up" : "arrow-down"}`} />
              ))}
          </>
        )}
      </button>

      {isOpen && (
        <div
          className={`dropdown-menu animation-${styles.animation || "slide"}`}
        >
          <ul className="dropdown-list">
            {items.map((item:any) => (
              <li
                key={item.id}
                className={`dropdown-item ${item.disabled ? "disabled" : ""}`}
                onMouseEnter={() => setActiveSubmenu(item.id)} // Hiển thị submenu khi hover
                onMouseLeave={() => setActiveSubmenu(null)} // Ẩn submenu khi rời chuột
              >
                <a
                  href={item.href}
                  className="dropdown-link"
                  onClick={(e) => {
                    e.preventDefault();
                    handleItemClick(item);
                  }}
                >
                  {renderDropdownItem(item)}
                </a>

                {item.children && activeSubmenu === item.id && (
                  <ul
                    className={`dropdown-submenu submenu-${
                      styles.submenuPosition || "right"
                    }`}
                  >
                    {item.children.map((child:any) => (
                      <li
                        key={child.id}
                        className={`dropdown-item ${child.disabled ? "disabled" : ""}`}
                      >
                        <a
                          href={child.href}
                          className="dropdown-link"
                          onClick={(e) => {
                            e.preventDefault();
                            handleItemClick(child);
                          }}
                        >
                          {renderDropdownItem(child)}
                        </a>
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};



export default MenuDrop;
