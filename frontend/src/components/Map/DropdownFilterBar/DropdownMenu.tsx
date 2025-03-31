import { useEffect, useMemo, useRef, useState } from 'react';
import { FaUser, FaSearch } from 'react-icons/fa';
import { ImSpoonKnife } from 'react-icons/im';
import styled from 'styled-components';
import DropdownItem from './DropdownItem';
import useDetectClose from '@/hooks/useDetectClose';

interface Option {
  label: string;
}

export interface DropdownMenuProps {
  options: Option[];
  onChange: (value: { main: string; sub?: string; lat?: number; lng?: number }) => void;
  isMobileOpen: boolean;
  placeholder?: string;
  type: 'influencer' | 'category';
  width: number;
  defaultValue?: string;
  selectedOptions?: string[];
}

export default function DropdownMenu({
  options,
  onChange,
  isMobileOpen = false,
  placeholder = '',
  type,
  width,
  defaultValue,
  selectedOptions,
}: DropdownMenuProps) {
  const [isOpen, setIsOpen] = useState(isMobileOpen);
  const ref = useDetectClose({ onDetected: () => setIsOpen(false) });
  const [selectedMainOption, setSelectedMainOption] = useState<Option | null>();
  const [searchTerm, setSearchTerm] = useState('');
  const isInitialized = useRef(false);

  useEffect(() => {
    if (isInitialized.current || !defaultValue || !options?.length) {
      return;
    }

    const mainOption = options.find((option) => option.label === defaultValue);
    if (!mainOption) {
      return;
    }
    setSelectedMainOption(mainOption);
    onChange({
      main: mainOption.label,
    });

    isInitialized.current = true;
  }, [defaultValue, options, onChange]);

  const filteredOptions = useMemo(() => {
    try {
      return (
        options?.filter((option) => {
          const mainMatch = option?.label?.toLowerCase().includes(searchTerm.toLowerCase());
          return mainMatch;
        }) || []
      );
    } catch {
      return [];
    }
  }, [options, searchTerm]);

  const handleMainOptionClick = (option: Option) => {
    setSelectedMainOption(option);
    onChange({
      main: option.label,
    });
    setIsOpen(false);
    setSelectedMainOption(null);
  };

  const handleSearchInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleDropdownToggle = () => {
    if (isOpen) {
      setIsOpen(false);
      setSelectedMainOption(null);
      setSearchTerm('');
    } else {
      setIsOpen(true);
    }
  };

  const renderMainOptions = () => {
    return filteredOptions.map((option) => {
      const isFiltered = selectedOptions && selectedOptions.includes(option.label);

      return (
        <DropdownItem
          key={option.label}
          label={option.label}
          onClick={() => handleMainOptionClick(option)}
          type={type}
          isSelected={selectedMainOption?.label === option.label}
          isFiltered={isFiltered}
        />
      );
    });
  };

  const displayValue = placeholder;
  const displayIcon = () => {
    if (type === 'category') {
      return <ImSpoonKnife color="#5ae3fb" />;
    }
    return <FaUser color="#5ae3fb" />;
  };
  return (
    <DropdownContainer ref={ref} type={type} $width={width}>
      <DropdownButton aria-label="dropdown_btn" $isOpen={isOpen} onClick={handleDropdownToggle}>
        {displayIcon()}
        {displayValue}
      </DropdownButton>
      {isOpen && (
        <DropdownMenuContainer $type={type}>
          <SearchInputContainer>
            <SearchInput placeholder="검색" value={searchTerm} onChange={handleSearchInputChange} />
            <SearchIcon />
          </SearchInputContainer>
          <OptionsContainer>
            <MainOptions>{renderMainOptions()}</MainOptions>
          </OptionsContainer>
        </DropdownMenuContainer>
      )}
    </DropdownContainer>
  );
}

const DropdownContainer = styled.div<{ type: 'influencer' | 'category'; $width: number }>`
  position: relative;
  height: 100%;
  width: ${({ $width }) => `${$width}px`};
  align-items: center;
  align-content: center;

  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const DropdownButton = styled.button<{ $isOpen: boolean }>`
  width: 100%;
  height: 100%;
  border: none;
  background-color: ${({ $isOpen }) => ($isOpen ? '#e8f9ff' : '#ffffff')};
  border-radius: 16px;
  display: flex;
  color: #333333;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  &:hover {
    background-color: #e8f9ff;
    border: none;
  }

  @media screen and (max-width: 768px) {
    padding: 6px 8px;
    font-size: 14px;
  }
`;

const DropdownMenuContainer = styled.div<{
  $type: 'influencer' | 'category';
}>`
  position: absolute;
  top: 100%;
  ${({ $type }) => {
    if ($type === 'category') {
      return `
        right: 0;
      `;
    }
    return `
        left: 0;
      `;
  }}
  width: 150%;
  background: #ffffff;
  color: #333333;
  box-shadow: 0 3px 12px 0 rgb(0 0 0/0.15);
  padding: 2px;
  box-sizing: border-box;
  border-radius: 8px;
  margin-top: 4px;
  max-height: 300px;
  z-index: 101;

  @media screen and (max-width: 768px) {
    max-height: 200px;
    margin-top: 2px;
    border-radius: 4px;
    overflow: hidden;
  }
`;

const SearchInputContainer = styled.div`
  position: relative;
  width: 100%;

  @media screen and (max-width: 768px) {
    padding: 2px 4px;
  }
`;

const SearchInput = styled.input`
  width: 100%;
  padding: 10px;
  padding-right: 30px;
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  outline: none;
  font-size: 16px;
  box-sizing: border-box;

  @media screen and (max-width: 768px) {
    padding: 6px;
    padding-right: 24px;
    font-size: 16px;
  }
`;

const SearchIcon = styled(FaSearch)`
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #a3a3a3;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    right: 12px;
    width: 14px;
    height: 14px;
  }
`;

const OptionsContainer = styled.div`
  display: flex;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;

const MainOptions = styled.div`
  flex: 1;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;
