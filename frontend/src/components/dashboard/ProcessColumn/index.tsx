import { Process } from '@customTypes/process';
import useProcess from '@hooks/useProcess';
import useApplicant from '@hooks/useApplicant';

import S from './style';
import ApplicantCard from '../ApplicantCard';

interface ProcessColumnProps {
  process: Process;
}

export default function ProcessColumn({ process }: ProcessColumnProps) {
  const { processList } = useProcess();
  const { moveApplicantProcess } = useApplicant();

  const menuItemsList = ({ applicantId }: { applicantId: number }) =>
    processList.map(({ processName, processId }) => ({
      id: processId,
      name: processName,
      onClick: ({ targetProcessId }: { targetProcessId: number }) => {
        console.log(targetProcessId, applicantId);
        moveApplicantProcess.mutate({ processId: targetProcessId, applicants: [applicantId] });
      },
    }));

  return (
    <S.ProcessWrapper>
      <S.Header>
        <S.Title>{process.name}</S.Title>
      </S.Header>
      <S.ApplicantList>
        {process.applicants.map(({ applicantId, applicantName, createdAt }) => (
          <ApplicantCard
            key={applicantId}
            name={applicantName}
            createdAt={createdAt}
            popOverMenuItems={menuItemsList({ applicantId })}
          />
        ))}
      </S.ApplicantList>
    </S.ProcessWrapper>
  );
}
