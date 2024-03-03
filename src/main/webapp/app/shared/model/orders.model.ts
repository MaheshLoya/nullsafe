import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';
import { ITransactions } from 'app/shared/model/transactions.model';
import { IProduct } from 'app/shared/model/product.model';
import { IUserAddress } from 'app/shared/model/user-address.model';
import { IOrderUserAssign } from 'app/shared/model/order-user-assign.model';
import { ISubscribedOrderDelivery } from 'app/shared/model/subscribed-order-delivery.model';

export interface IOrders {
  id?: number;
  orderType?: number | null;
  orderAmount?: number;
  price?: number;
  mrp?: number;
  tax?: number;
  qty?: number | null;
  selectedDaysForWeekly?: string | null;
  startDate?: dayjs.Dayjs | null;
  subscriptionType?: number | null;
  status?: number;
  deliveryStatus?: number | null;
  orderStatus?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  user?: IUsers;
  trasation?: ITransactions | null;
  product?: IProduct;
  address?: IUserAddress;
  orderUserAssigns?: IOrderUserAssign[] | null;
  subscribedOrderDeliveries?: ISubscribedOrderDelivery[] | null;
  transactions?: ITransactions[] | null;
}

export const defaultValue: Readonly<IOrders> = {
  orderStatus: false,
};
