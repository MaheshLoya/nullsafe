import dayjs from 'dayjs';

export interface ISocialMedia {
  id?: number;
  title?: string;
  image?: string;
  url?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ISocialMedia> = {};
