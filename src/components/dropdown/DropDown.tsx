// types/dropdown.ts
export interface DropdownOption {
  value: string | number;
  label: string;
  disabled?: boolean;
}

export interface DropdownProps {
  options: DropdownOption[];
  value?: string | number;
  onChange?: (value: string | number) => void;
  placeholder?: string;
  disabled?: boolean;
  error?: string;
  searchable?: boolean;
  className?: string;
}

// styles/_dropdown.scss


// components/Dropdown/Dropdown.tsx
import React, { useState, useRef, useEffect } from 'react';

import './DropDown.scss';

export const Dropdown: React.FC<DropdownProps> = ({
  options,
  value,
  onChange,
  placeholder = 'Select an option',
  disabled = false,
  error,
  searchable = false,
  className = ''
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const dropdownRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    if (isOpen && searchable && searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [isOpen, searchable]);

  const filteredOptions = options.filter(option =>
    option.label.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleOptionClick = (option: DropdownOption) => {
    if (!option.disabled) {
      onChange?.(option.value);
      setIsOpen(false);
      setSearchTerm('');
    }
  };

  const selectedOption = options.find(option => option.value === value);

  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (disabled) return;

    switch (event.key) {
      case 'Enter':
      case ' ':
        event.preventDefault();
        setIsOpen(!isOpen);
        break;
      case 'Escape':
        setIsOpen(false);
        break;
      case 'ArrowUp':
      case 'ArrowDown':
        if (!isOpen) {
          event.preventDefault();
          setIsOpen(true);
        }
        break;
    }
  };

  const handleButtonClick = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
    }
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  return (
    <div 
      ref={dropdownRef} 
      className={`dropdown ${className}`}
    >
      <button
        type="button"
        className={`
          dropdown__button
          ${disabled ? 'dropdown__button--disabled' : ''}
          ${error ? 'dropdown__button--error' : ''}
        `}
        onClick={handleButtonClick}
        onKeyDown={handleKeyDown}
        aria-haspopup="listbox"
        aria-expanded={isOpen}
        disabled={disabled}
      >
        <span className={!selectedOption ? 'dropdown__placeholder' : ''}>
          {selectedOption ? selectedOption.label : placeholder}
        </span>
        <span className={`dropdown__arrow ${isOpen ? 'dropdown__arrow--open' : ''}`} />
      </button>

      {error && <div className="dropdown__error">{error}</div>}

      <div className={`dropdown__menu ${isOpen ? 'dropdown__menu--open' : ''}`}>
        {searchable && (
          <div className="dropdown__search">
            <input
              ref={searchInputRef}
              type="text"
              value={searchTerm}
              onChange={handleSearchChange}
              placeholder="Search..."
              autoComplete="off"
            />
          </div>
        )}
        
        <div className="dropdown__options" role="listbox">
          {filteredOptions.map(option => (
            <div
              key={option.value}
              className={`
                dropdown__option
                ${value === option.value ? 'dropdown__option--selected' : ''}
                ${option.disabled ? 'dropdown__option--disabled' : ''}
              `}
              onClick={() => handleOptionClick(option)}
              role="option"
              aria-selected={value === option.value}
              aria-disabled={option.disabled}
            >
              {option.label}
            </div>
          ))}
          {filteredOptions.length === 0 && (
            <div className="dropdown__option dropdown__option--disabled">
              No options found
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dropdown;