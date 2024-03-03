import dayjs from 'dayjs';

export interface IPaymentGateway {
  id?: number;
  active?: boolean;
  title?: string;
  keyId?: string;
  secretId?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPaymentGateway> = {
  active: false,
};
