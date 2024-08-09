import RecruitmentSidebar from '@components/recruitment/RecruitmentSidebar';
import useDashboardCreateForm from '@hooks/useDashboardCreateForm';
import Recruitment from '@components/dashboard/DashboardCreate/Recruitment';
import Apply from '@components/dashboard/DashboardCreate/Apply';
import Finish from '@components/dashboard/DashboardCreate/Finish';

import { useParams } from 'react-router-dom';

import S from './style';

export default function DashboardCreate() {
  const {
    stepState,
    prevStep,
    nextStep,

    recruitmentInfoState,
    setRecruitmentInfoState,
    applyState,

    addQuestion,
    setQuestionTitle,
    setQuestionType,
    setQuestionOptions,
    setQuestionRequiredToggle,
    setQuestionPrev,
    setQuestionNext,
    deleteQuestion,

    finishResJson,
  } = useDashboardCreateForm();

  const { dashboardId } = useParams() as { dashboardId: string };

  const isRecruitmentStep = stepState === 'recruitmentForm';
  const isApplyStep = stepState === 'applyForm';
  const isFinish = stepState === 'finished';

  return (
    <S.Layout>
      <RecruitmentSidebar
        options={[
          { text: '공고 작성', isSelected: isRecruitmentStep },
          { text: '지원서 작성', isSelected: stepState === 'applyForm' },
          { text: '공고 게시', isSelected: isFinish },
        ]}
      />

      <S.MainContainer>
        {isRecruitmentStep && (
          <Recruitment
            recruitmentInfoState={recruitmentInfoState}
            setRecruitmentInfoState={setRecruitmentInfoState}
            nextStep={nextStep}
          />
        )}

        {isApplyStep && (
          <Apply
            applyState={applyState}
            addQuestion={addQuestion}
            setQuestionTitle={setQuestionTitle}
            setQuestionType={setQuestionType}
            setQuestionOptions={setQuestionOptions}
            setQuestionRequiredToggle={setQuestionRequiredToggle}
            setQuestionPrev={setQuestionPrev}
            setQuestionNext={setQuestionNext}
            deleteQuestion={deleteQuestion}
            prevStep={prevStep}
            nextStep={() => nextStep(dashboardId)}
          />
        )}

        {finishResJson && (
          <Finish
            postId={finishResJson.postId}
            postUrl={finishResJson.postUrl}
          />
        )}
      </S.MainContainer>
    </S.Layout>
  );
}
