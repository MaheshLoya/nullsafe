import dayjs from 'dayjs';

export interface IAppSettings {
  id?: number;
  settingId?: number;
  title?: string;
  value?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IAppSettings> = {};
