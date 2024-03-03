import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserNotification from './user-notification';
import UserNotificationDetail from './user-notification-detail';
import UserNotificationUpdate from './user-notification-update';
import UserNotificationDeleteDialog from './user-notification-delete-dialog';

const UserNotificationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserNotification />} />
    <Route path="new" element={<UserNotificationUpdate />} />
    <Route path=":id">
      <Route index element={<UserNotificationDetail />} />
      <Route path="edit" element={<UserNotificationUpdate />} />
      <Route path="delete" element={<UserNotificationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserNotificationRoutes;
