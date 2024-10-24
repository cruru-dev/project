import styled from '@emotion/styled';
import { hideScrollBar } from '@styles/utils';

const Container = styled.div<{ isSidebarOpen: boolean }>`
  position: relative;
  width: ${({ isSidebarOpen }) => (isSidebarOpen ? '276px' : '56px')};
  height: 100%;
  border-right: 1px solid ${({ theme }) => theme.baseColors.grayscale[400]};
  padding: 3.6rem 1.6rem 6.4rem;

  background-color: ${({ theme }) => theme.baseColors.grayscale[50]};

  display: flex;
  flex-direction: column;
  gap: 4rem;
`;

const SidebarHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 2.4rem;
`;

const Logo = styled.img`
  height: 2.4rem;
`;

const SidebarToggleIcon = styled.div`
  color: ${({ theme }) => theme.baseColors.grayscale[500]};
`;

const SidebarNav = styled.nav`
  flex: 1;
  overflow: hidden;
`;

const Contents = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-height: 100%;
`;

const SidebarScrollBox = styled.div`
  overflow-y: scroll;
  ${hideScrollBar};
`;

const SidebarItemGroup = styled.li`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  margin-bottom: 2.4rem;
`;

const SidebarItem = styled.li<{ isSidebarOpen: boolean }>`
  display: flex;
  align-items: center;

  list-style: none;
  height: 2.4rem;

  & .sidebar-tooltip {
    position: absolute;
    transform: ${({ isSidebarOpen }) => (isSidebarOpen ? 'translate(26rem, 1.3rem)' : 'translate(4rem, 1.3rem)')};
    transition: all 0.5s ease;
    z-index: 5;

    background: ${({ theme }) => theme.baseColors.grayscale[900]};
    ${({ theme }) => theme.typography.common.block};
    color: ${({ theme }) => theme.baseColors.grayscale[100]};
    line-height: inherit;

    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
    border-radius: 0.8rem;
    padding: 0.8rem 1.2rem;
    opacity: 0;
    white-space: nowrap;
    pointer-events: none;
  }

  &:hover .sidebar-tooltip {
    opacity: 1;
    pointer-events: auto;
    transform: ${({ isSidebarOpen }) => (isSidebarOpen ? 'translate(26.5rem, 1.3rem)' : 'translate(4.5rem, 1.3rem)')};
  }
`;

const SidebarItemLink = styled.div<{ isSelected: boolean; isSidebarOpen?: boolean }>`
  display: flex;
  align-items: center;
  justify-content: ${({ isSidebarOpen }) => (isSidebarOpen ? 'left' : 'center')};

  gap: 1rem;
  color: ${({ theme }) => theme.baseColors.grayscale[900]};
  opacity: ${({ isSelected }) => (isSelected ? 0.99 : 0.4)};

  transition: opacity 0.2s ease;

  cursor: pointer;

  &:hover {
    opacity: 0.99;
  }
`;

const SidebarItemTextHeader = styled.p`
  width: 21rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  ${({ theme }) => theme.typography.common.largeAccent}
`;

const SidebarItemText = styled.p`
  width: 21rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  ${({ theme }) => theme.typography.common.largeBlock}
`;

const Divider = styled.div`
  border-bottom: 0.1rem solid ${({ theme }) => theme.baseColors.grayscale[400]};
`;

const ContentSubTitle = styled.div`
  height: 2rem;
  color: ${({ theme }) => theme.baseColors.grayscale[500]};
  ${({ theme }) => theme.typography.common.smallBlock}

  margin-bottom: -0.6rem;
`;

const IconContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  width: 24px;
  aspect-ratio: 1/1;
`;

const Circle = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;

  &::before {
    content: '•';
    scale: 1;
  }
`;

const S = {
  Container,
  SidebarHeader,
  Logo,
  SidebarToggleIcon,
  Contents,
  SidebarNav,
  SidebarScrollBox,
  SidebarItemGroup,
  SidebarItem,
  SidebarItemLink,
  SidebarItemTextHeader,
  SidebarItemText,
  Divider,
  ContentSubTitle,
  IconContainer,
  Circle,
};

export default S;
