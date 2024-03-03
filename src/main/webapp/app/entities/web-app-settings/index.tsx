import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WebAppSettings from './web-app-settings';
import WebAppSettingsDetail from './web-app-settings-detail';
import WebAppSettingsUpdate from './web-app-settings-update';
import WebAppSettingsDeleteDialog from './web-app-settings-delete-dialog';

const WebAppSettingsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WebAppSettings />} />
    <Route path="new" element={<WebAppSettingsUpdate />} />
    <Route path=":id">
      <Route index element={<WebAppSettingsDetail />} />
      <Route path="edit" element={<WebAppSettingsUpdate />} />
      <Route path="delete" element={<WebAppSettingsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WebAppSettingsRoutes;
