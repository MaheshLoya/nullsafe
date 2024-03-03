import dayjs from 'dayjs';
import { ISubCat } from 'app/shared/model/sub-cat.model';

export interface ICat {
  id?: number;
  title?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  isActive?: boolean;
  subCats?: ISubCat[] | null;
}

export const defaultValue: Readonly<ICat> = {
  isActive: false,
};
