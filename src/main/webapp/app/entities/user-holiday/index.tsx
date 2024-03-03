import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserHoliday from './user-holiday';
import UserHolidayDetail from './user-holiday-detail';
import UserHolidayUpdate from './user-holiday-update';
import UserHolidayDeleteDialog from './user-holiday-delete-dialog';

const UserHolidayRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserHoliday />} />
    <Route path="new" element={<UserHolidayUpdate />} />
    <Route path=":id">
      <Route index element={<UserHolidayDetail />} />
      <Route path="edit" element={<UserHolidayUpdate />} />
      <Route path="delete" element={<UserHolidayDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserHolidayRoutes;
