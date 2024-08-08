import styled from '@emotion/styled';
import { hiddenStyles, hideScrollBar, visibleStyles } from '@styles/utils';

const AppContainer = styled.div`
  padding: 3.6rem 2rem;
`;

const Title = styled.div`
  ${({ theme }) => theme.typography.heading[700]}
`;

const TabWrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

const DashboardPanel = styled.div<{ isVisible: boolean }>`
  width: 100%;
  height: 80%;
  padding: 2rem;

  overflow-x: scroll;
  ${hideScrollBar};

  transition: transform 0.2s ease-in-out;
  transform: translateY(${({ isVisible }) => (isVisible ? '0' : '1rem')});

  ${({ isVisible }) => (isVisible ? visibleStyles : hiddenStyles)};
`;

const S = {
  AppContainer,
  TabWrapper,
  Title,
  DashboardPanel,
};

export default S;
