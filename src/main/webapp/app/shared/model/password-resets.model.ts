import dayjs from 'dayjs';

export interface IPasswordResets {
  id?: number;
  email?: string;
  token?: string;
  createdAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPasswordResets> = {};
