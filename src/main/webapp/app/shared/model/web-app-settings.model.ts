import dayjs from 'dayjs';

export interface IWebAppSettings {
  id?: number;
  title?: string;
  value?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IWebAppSettings> = {};
