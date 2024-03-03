import dayjs from 'dayjs';
import { IOrders } from 'app/shared/model/orders.model';
import { ISubscribedOrders } from 'app/shared/model/subscribed-orders.model';

export interface IUserAddress {
  id?: number;
  userId?: number;
  name?: string;
  sPhone?: string;
  flatNo?: string | null;
  apartmentName?: string | null;
  area?: string;
  landmark?: string;
  city?: string;
  pincode?: number;
  lat?: number | null;
  lng?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  orders?: IOrders[] | null;
  subscribedOrders?: ISubscribedOrders[] | null;
}

export const defaultValue: Readonly<IUserAddress> = {
  isActive: false,
};
