import dayjs from 'dayjs';

export interface ITestimonials {
  id?: number;
  title?: string;
  subTitle?: string;
  rating?: number;
  description?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ITestimonials> = {};
