import dayjs from 'dayjs';

export interface IFiles {
  id?: number;
  name?: string;
  fileUrl?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deleted?: boolean;
  fileFor?: number;
  fileForId?: number;
  fileCat?: boolean;
}

export const defaultValue: Readonly<IFiles> = {
  deleted: false,
  fileCat: false,
};
