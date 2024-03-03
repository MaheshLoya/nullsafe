import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { IUsers } from 'app/shared/model/users.model';

export interface ICart {
  id?: number;
  qty?: number;
  price?: number;
  totalPrice?: number;
  mrp?: number;
  tax?: number;
  qtyText?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  product?: IProduct;
  user?: IUsers;
}

export const defaultValue: Readonly<ICart> = {};
