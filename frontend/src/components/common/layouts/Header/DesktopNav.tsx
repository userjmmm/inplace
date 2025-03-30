import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { Text } from '@/components/common/typography/Text';
import useAuth from '@/hooks/useAuth';

export default function DesktopNav() {
  const location = useLocation();
  const { isAuthenticated } = useAuth();

  const isActive = (path: string) => {
    if (path.startsWith('http')) return false;
    return location.pathname === path;
  };

  const commonLinks = [
    { to: '/map', label: '지도' },
    { to: '/influencer', label: '인플루언서' },
  ];

  return (
    <NavLinksContainer>
      <NavItem
        to="https://docs.google.com/forms/d/e/1FAIpQLSeBJcQg0gcVv2au5oFZ1aCLF9O_qbEiJCvnLEd0d1SSLLpDUA/viewform?pli=1"
        target="_blank"
        rel="noopener noreferrer"
        $isActive={false}
      >
        <Text size="m" weight="normal">
          설문조사
        </Text>
      </NavItem>
      {commonLinks.map((link) => (
        <NavItem key={link.to} to={link.to} $isActive={isActive(link.to)}>
          <Text size="m" weight="normal">
            {link.label}
          </Text>
        </NavItem>
      ))}
      <NavItem
        to="/my"
        style={{
          visibility: isAuthenticated ? 'visible' : 'hidden',
          pointerEvents: isAuthenticated ? 'auto' : 'none',
        }}
        $isActive={isActive('/my')}
      >
        <Text size="m" weight="normal">
          마이페이지
        </Text>
      </NavItem>
    </NavLinksContainer>
  );
}

const NavLinksContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;

  @media (max-width: 768px) {
    display: none;
  }
`;

const NavItem = styled(Link)<{ $isActive: boolean }>`
  margin-left: 20px;
  text-decoration: none;
  cursor: pointer;
  color: inherit;
  position: relative;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -6px;
    width: 100%;
    height: 3px;
    background-color: #47c8d9;
    transform: ${({ $isActive }) => ($isActive ? 'scaleX(1)' : 'scaleX(0)')};
    transition: transform 0.3s ease;
  }

  &:hover::after {
    transform: scaleX(1);
  }
`;
