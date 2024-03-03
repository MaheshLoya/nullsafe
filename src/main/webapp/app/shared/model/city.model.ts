export interface ICity {
  id?: number;
  title?: string;
  deleted?: boolean;
}

export const defaultValue: Readonly<ICity> = {
  deleted: false,
};
