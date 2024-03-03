import dayjs from 'dayjs';

export interface IAllowPincode {
  id?: number;
  pinCode?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IAllowPincode> = {};
