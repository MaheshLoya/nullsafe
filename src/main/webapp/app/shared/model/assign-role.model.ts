import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';
import { IRole } from 'app/shared/model/role.model';

export interface IAssignRole {
  id?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  user?: IUsers;
  role?: IRole;
}

export const defaultValue: Readonly<IAssignRole> = {};
