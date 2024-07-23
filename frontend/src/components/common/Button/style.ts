import { css, SerializedStyles, Theme } from '@emotion/react';
import styled from '@emotion/styled';

export type ButtonSize = 'sm' | 'md';
export type ButtonColor = 'white' | 'black' | 'primary' | 'error';

export interface ButtonStyleProps {
  size: ButtonSize;
  color: ButtonColor;
}

const commonStyles = (theme: Theme) => css`
  border-radius: 0.8rem;
  ${theme.typography.common.default};

  transition: all 0.2s ease-in-out;

  &:hover {
    cursor: pointer;
  }
`;

const sizeStyles = {
  sm: css`
    padding: 0 0.4rem;
  `,
  md: css`
    padding: 0 1.2rem;
  `,
};

const colorStyles: { [key in ButtonColor]: (theme: Theme) => SerializedStyles } = {
  white: (theme: Theme) => css`
    color: ${theme.colors.text.default};
    background-color: ${theme.baseColors.grayscale[50]};
    border: 1px solid ${theme.baseColors.grayscale[500]};

    &:hover {
      background-color: ${theme.baseColors.grayscale[300]};
      border: 1px solid ${theme.baseColors.grayscale[800]};
    }
  `,
  black: (theme: Theme) => css`
    color: ${theme.baseColors.grayscale[50]};
    background-color: ${theme.baseColors.grayscale[900]};
    border: 1px solid ${theme.baseColors.grayscale[900]};

    &:hover {
      background-color: ${theme.baseColors.grayscale[950]};
      border: 1px solid ${theme.baseColors.grayscale[950]};
    }
  `,
  primary: (theme: Theme) => css`
    color: ${theme.colors.text.default};
    background-color: ${theme.baseColors.purplescale[50]};
    border: 1px solid ${theme.baseColors.purplescale[500]};

    &:hover {
      background-color: ${theme.baseColors.purplescale[200]};
      border: 1px solid ${theme.baseColors.purplescale[700]};
    }
  `,
  error: (theme: Theme) => css`
    color: ${theme.colors.feedback.error};
    background-color: ${theme.baseColors.grayscale[50]};
    border: 1px solid ${theme.colors.feedback.error};

    &:hover {
      color: ${theme.baseColors.grayscale[50]};
      background-color: ${theme.colors.feedback.error};
      border: 1px solid ${theme.colors.feedback.error};
    }
  `,
};

const Button = styled.button<ButtonStyleProps>`
  ${({ theme }) => commonStyles(theme)}
  ${({ size }) => sizeStyles[size]}
  ${({ color, theme }) => colorStyles[color](theme)}

  height: 100%;
`;

const S = {
  Button,
};

export default S;
