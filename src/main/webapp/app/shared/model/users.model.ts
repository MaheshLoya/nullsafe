import dayjs from 'dayjs';
import { IAssignRole } from 'app/shared/model/assign-role.model';
import { ICart } from 'app/shared/model/cart.model';
import { IOrderUserAssign } from 'app/shared/model/order-user-assign.model';
import { IOrders } from 'app/shared/model/orders.model';
import { ISpecificNotification } from 'app/shared/model/specific-notification.model';
import { ISubscribedOrderDelivery } from 'app/shared/model/subscribed-order-delivery.model';
import { ISubscribedOrders } from 'app/shared/model/subscribed-orders.model';
import { ITransactions } from 'app/shared/model/transactions.model';

export interface IUsers {
  id?: number;
  walletAmount?: number | null;
  email?: string | null;
  phone?: string | null;
  emailVerifiedAt?: dayjs.Dayjs | null;
  password?: string | null;
  rememberToken?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  name?: string;
  fcm?: string | null;
  subscriptionAmount?: number;
  assignRoles?: IAssignRole[] | null;
  carts?: ICart[] | null;
  orderUserAssigns?: IOrderUserAssign[] | null;
  orders?: IOrders[] | null;
  specificNotifications?: ISpecificNotification[] | null;
  subscribedOrderDeliveries?: ISubscribedOrderDelivery[] | null;
  subscribedOrders?: ISubscribedOrders[] | null;
  transactions?: ITransactions[] | null;
}

export const defaultValue: Readonly<IUsers> = {};
