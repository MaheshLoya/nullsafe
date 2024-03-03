import dayjs from 'dayjs';
import { ISubCat } from 'app/shared/model/sub-cat.model';
import { ICart } from 'app/shared/model/cart.model';
import { IOrders } from 'app/shared/model/orders.model';
import { ISubscribedOrders } from 'app/shared/model/subscribed-orders.model';

export interface IProduct {
  id?: number;
  title?: string;
  qtyText?: string;
  stockQty?: number | null;
  price?: number;
  tax?: number;
  mrp?: number;
  offerText?: string | null;
  description?: string | null;
  disclaimer?: string | null;
  subscription?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  isActive?: boolean;
  subCat?: ISubCat;
  carts?: ICart[] | null;
  orders?: IOrders[] | null;
  subscribedOrders?: ISubscribedOrders[] | null;
}

export const defaultValue: Readonly<IProduct> = {
  subscription: false,
  isActive: false,
};
