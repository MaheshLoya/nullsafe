import dayjs from 'dayjs';

export interface IImages {
  id?: number;
  tableName?: string;
  tableId?: number;
  imageType?: boolean;
  image?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IImages> = {
  imageType: false,
};
