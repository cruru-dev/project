import styled from '@emotion/styled';

const Form = styled.form`
  width: 100%;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3.2rem;
`;

const CheckBoxContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
`;

const CheckBoxOption = styled.div`
  display: flex;
  align-items: center;
  gap: 1.2rem;
`;

const CheckBoxLabel = styled.label`
  ${({ theme }) => theme.typography.common.large}
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const PersonalDataCollectionDescription = styled.p`
  ${({ theme }) => theme.typography.common.small};
  color: ${({ theme }) => theme.baseColors.grayscale[700]};
`;

const Divider = styled.div`
  width: 100%;
  height: 0.2rem;
  background-color: ${({ theme }) => theme.baseColors.grayscale[500]};
`;

const S = {
  Form,
  Divider,
  CheckBoxContainer,
  CheckBoxOption,
  CheckBoxLabel,
  PersonalDataCollectionDescription,
};

export default S;
