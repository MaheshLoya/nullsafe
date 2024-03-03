import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';

export interface ISpecificNotification {
  id?: number;
  title?: string;
  body?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  user?: IUsers;
}

export const defaultValue: Readonly<ISpecificNotification> = {};
