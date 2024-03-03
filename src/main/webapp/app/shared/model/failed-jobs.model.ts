import dayjs from 'dayjs';

export interface IFailedJobs {
  id?: number;
  uuid?: string;
  connection?: string;
  queue?: string;
  payload?: string;
  exception?: string;
  failedAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IFailedJobs> = {};
