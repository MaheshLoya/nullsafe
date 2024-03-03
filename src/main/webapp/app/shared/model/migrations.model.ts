export interface IMigrations {
  id?: number;
  migration?: string;
  batch?: number;
}

export const defaultValue: Readonly<IMigrations> = {};
