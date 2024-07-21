import styled from '@emotion/styled';

const Item = styled.button<{ isHighlight: boolean; size: 'sm' | 'md' }>`
  display: block;
  text-align: left;
  width: 100%;

  padding: ${({ size }) => (size === 'md' ? '6px 8px' : '6px 4px')};
  ${({ theme, size }) => (size === 'md' ? theme.typography.common.default : theme.typography.common.small)};
  color: ${({ isHighlight, theme }) => (isHighlight ? theme.baseColors.redscale[500] : 'none')};
  border-radius: 4px;

  cursor: pointer;
  transition: all 0.2s ease-in-out;

  &:hover {
    background-color: ${({ isHighlight, theme }) =>
      isHighlight ? theme.baseColors.redscale[300] : theme.baseColors.grayscale[300]};
    color: ${({ isHighlight, theme }) => (isHighlight ? theme.baseColors.grayscale[50] : 'none')};
  }
`;

const S = {
  Item,
};

export default S;
