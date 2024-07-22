export interface ApplicantCardInfo {
  applicantId: number;
  applicantName: string;
  createdAt: string;
}

export interface Process {
  processId: number;
  orderIndex: number;
  name: string;
  description: string;
  applicants: ApplicantCardInfo[];
}
