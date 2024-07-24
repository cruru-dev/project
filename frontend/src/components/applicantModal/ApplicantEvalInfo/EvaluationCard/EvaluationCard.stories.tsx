import type { Meta, StoryObj } from '@storybook/react';
import EvaluationCard from './index';

const meta: Meta<typeof EvaluationCard> = {
  title: 'Components/ApplicantModal/ApplicantEvalInfo',
  component: EvaluationCard,
  parameters: {
    layout: 'centered',
    docs: {
      description: {
        component: '모달의 지원자에 대한 개별 평가 카드 컴포넌트입니다.',
      },
    },
  },
  tags: ['autodocs'],
  decorators: [
    (Story) => (
      <div style={{ padding: '20px', minWidth: '400px' }}>
        <Story />
      </div>
    ),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    evaluatorName: '평가자 이름',
    evaluatedDate: '2024-07-16T05:46:08.328593',
    score: 4,
    comment: '지원자에 대한 평가자의 코멘트가 들어가는 영역입니다.',
  },
};

Default.parameters = {
  docs: {},
};
