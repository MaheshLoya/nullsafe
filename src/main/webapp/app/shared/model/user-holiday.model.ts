import dayjs from 'dayjs';

export interface IUserHoliday {
  id?: number;
  userId?: number;
  date?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IUserHoliday> = {};
