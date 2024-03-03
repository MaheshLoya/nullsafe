import dayjs from 'dayjs';
import { IOrders } from 'app/shared/model/orders.model';
import { IUsers } from 'app/shared/model/users.model';

export interface IOrderUserAssign {
  id?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  order?: IOrders;
  user?: IUsers;
}

export const defaultValue: Readonly<IOrderUserAssign> = {};
