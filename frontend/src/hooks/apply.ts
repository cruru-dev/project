import applyApis from '@api/apply';
import { ApplyRequestBody } from '@customTypes/apply';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import QUERY_KEYS from './queryKeys';

const useGetRecruitmentInfo = ({ postId }: { postId: string }) => {
  const queryObj = useQuery({
    queryKey: [QUERY_KEYS.RECRUITMENT_INFO, postId],
    queryFn: () => applyApis.get({ postId }),
    staleTime: 1000 * 60 * 10, // 10 minutes
  });

  return queryObj;
};

export const applyQueries = {
  useGetRecruitmentPost: ({ postId }: { postId: string }) => {
    const { data, ...restQueryObj } = useGetRecruitmentInfo({ postId });

    const { startDate, endDate } = data?.recruitmentPost ?? { startDate: '', endDate: '' };
    const start = new Date(startDate as string).getTime();
    const end = new Date(endDate as string).getTime();
    const now = new Date().getTime();

    const isClosed = !data || end < now || start > now;

    return {
      isClosed,
      data: data?.recruitmentPost,
      ...restQueryObj,
    };
  },

  useGetApplyForm: ({ postId }: { postId: string }) => {
    const { data, ...restQueryObj } = useGetRecruitmentInfo({ postId });

    return {
      data: data?.applyForm.questions,
      ...restQueryObj,
    };
  },
};

export const applyMutations = {
  useApply: (postId: string, title: string) => {
    const navigate = useNavigate();

    return useMutation({
      mutationFn: (params: { body: ApplyRequestBody }) => applyApis.apply({ ...params, postId }),
      onError: (error) => {
        window.alert(error.message);
      },
      onSuccess: () => {
        navigate(`/post/${postId}/confirm`, { state: { title } });
      },
    });
  },
};
