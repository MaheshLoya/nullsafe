import dayjs from 'dayjs';

export interface IUserNotification {
  id?: number;
  title?: string;
  body?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IUserNotification> = {};
