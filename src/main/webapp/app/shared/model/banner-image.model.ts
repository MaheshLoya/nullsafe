import dayjs from 'dayjs';

export interface IBannerImage {
  id?: number;
  image?: string;
  imageType?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IBannerImage> = {
  imageType: false,
};
