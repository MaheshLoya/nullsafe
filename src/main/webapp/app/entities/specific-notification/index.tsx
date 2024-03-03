import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecificNotification from './specific-notification';
import SpecificNotificationDetail from './specific-notification-detail';
import SpecificNotificationUpdate from './specific-notification-update';
import SpecificNotificationDeleteDialog from './specific-notification-delete-dialog';

const SpecificNotificationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecificNotification />} />
    <Route path="new" element={<SpecificNotificationUpdate />} />
    <Route path=":id">
      <Route index element={<SpecificNotificationDetail />} />
      <Route path="edit" element={<SpecificNotificationUpdate />} />
      <Route path="delete" element={<SpecificNotificationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecificNotificationRoutes;
