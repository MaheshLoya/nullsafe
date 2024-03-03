import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';
import { ITransactions } from 'app/shared/model/transactions.model';
import { IProduct } from 'app/shared/model/product.model';
import { IUserAddress } from 'app/shared/model/user-address.model';

export interface ISubscribedOrders {
  id?: number;
  paymentType?: number | null;
  orderAmount?: number;
  subscriptionBalanceAmount?: number | null;
  price?: number;
  mrp?: number;
  tax?: number;
  qty?: number | null;
  offerId?: number | null;
  selectedDaysForWeekly?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  lastRenewalDate?: dayjs.Dayjs | null;
  subscriptionType?: number | null;
  approvalStatus?: number;
  orderStatus?: boolean;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  user?: IUsers;
  transaction?: ITransactions | null;
  product?: IProduct;
  address?: IUserAddress;
}

export const defaultValue: Readonly<ISubscribedOrders> = {
  orderStatus: false,
};
