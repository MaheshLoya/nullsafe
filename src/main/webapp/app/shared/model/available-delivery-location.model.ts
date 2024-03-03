import dayjs from 'dayjs';

export interface IAvailableDeliveryLocation {
  id?: number;
  title?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IAvailableDeliveryLocation> = {};
