import dayjs from 'dayjs';
import { IAssignRole } from 'app/shared/model/assign-role.model';

export interface IRole {
  id?: number;
  title?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deleted?: boolean;
  assignRoles?: IAssignRole[] | null;
}

export const defaultValue: Readonly<IRole> = {
  deleted: false,
};
