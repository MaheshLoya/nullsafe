import dayjs from 'dayjs';
import { IOrders } from 'app/shared/model/orders.model';
import { IUsers } from 'app/shared/model/users.model';

export interface ISubscribedOrderDelivery {
  id?: number;
  date?: dayjs.Dayjs | null;
  paymentMode?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  order?: IOrders;
  entryUser?: IUsers;
}

export const defaultValue: Readonly<ISubscribedOrderDelivery> = {};
