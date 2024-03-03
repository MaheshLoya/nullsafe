import dayjs from 'dayjs';

export interface IWebPages {
  id?: number;
  pageId?: number;
  title?: string;
  body?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IWebPages> = {};
