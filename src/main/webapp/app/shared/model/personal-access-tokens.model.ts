import dayjs from 'dayjs';

export interface IPersonalAccessTokens {
  id?: number;
  tokenableType?: string;
  tokenableId?: number;
  name?: string;
  token?: string;
  abilities?: string | null;
  lastUsedAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPersonalAccessTokens> = {};
