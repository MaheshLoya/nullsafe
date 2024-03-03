import dayjs from 'dayjs';
import { IOrders } from 'app/shared/model/orders.model';
import { IUsers } from 'app/shared/model/users.model';
import { ISubscribedOrders } from 'app/shared/model/subscribed-orders.model';

export interface ITransactions {
  id?: number;
  paymentId?: string | null;
  amount?: number;
  description?: string | null;
  type?: number | null;
  paymentMode?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  order?: IOrders | null;
  user?: IUsers;
  orders?: IOrders[] | null;
  subscribedOrders?: ISubscribedOrders[] | null;
}

export const defaultValue: Readonly<ITransactions> = {};
