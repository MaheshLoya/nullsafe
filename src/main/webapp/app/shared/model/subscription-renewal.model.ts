import dayjs from 'dayjs';

export interface ISubscriptionRenewal {
  id?: number;
  userId?: number;
  orderId?: number;
  transactionId?: number | null;
  renewalDate?: dayjs.Dayjs;
  paidRenewalAmount?: number;
  status?: boolean;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ISubscriptionRenewal> = {
  status: false,
};
