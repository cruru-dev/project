import { PROCESSES } from './endPoint';
import { createParams } from './utils';
import ApiError from './ApiError';

const processApis = {
  get: async ({ id }: { id: number }) => {
    const response = await fetch(`${PROCESSES}?${createParams({ dashboard_id: String(id) })}`, {
      headers: {
        Accept: 'application/json',
      },
    });

    if (!response.ok) {
      throw new ApiError({
        method: 'GET',
        statusCode: response.status,
        message: '프로세스 목록을 불러오지 못했습니다.',
      });
    }

    const data = await response.json();
    return data;
  },

  create: async (params: { dashboardId: number; orderIndex: number; name: string; description?: string }) => {
    const response = await fetch(`${PROCESSES}?${createParams({ dashboard_id: String(params.dashboardId) })}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        orderIndex: params.orderIndex,
        processName: params.name,
        description: params?.description,
      }),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    return response;
  },

  modify: async (params: { processId: number; name: string; description?: string }) => {
    const response = await fetch(`${PROCESSES}/${params.processId}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        processName: params.name,
        description: params?.description,
      }),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    return response;
  },

  delete: async ({ processId }: { processId: number }) => {
    const response = await fetch(`${PROCESSES}/${processId}`, {
      method: 'DELETE',
      headers: {
        Accept: 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    return response;
  },
};

export default processApis;
