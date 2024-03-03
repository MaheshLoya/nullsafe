import dayjs from 'dayjs';

export interface IRefunds {
  id?: number;
  orderId?: number | null;
  transactionId?: string | null;
  razorpayRefundId?: string | null;
  razorpayPaymentId?: string | null;
  amount?: number | null;
  currency?: string | null;
  status?: string | null;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IRefunds> = {};
