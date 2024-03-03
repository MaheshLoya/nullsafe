import dayjs from 'dayjs';
import { ICat } from 'app/shared/model/cat.model';
import { IProduct } from 'app/shared/model/product.model';

export interface ISubCat {
  id?: number;
  title?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  isActive?: boolean;
  cat?: ICat;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<ISubCat> = {
  isActive: false,
};
