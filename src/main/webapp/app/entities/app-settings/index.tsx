import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AppSettings from './app-settings';
import AppSettingsDetail from './app-settings-detail';
import AppSettingsUpdate from './app-settings-update';
import AppSettingsDeleteDialog from './app-settings-delete-dialog';

const AppSettingsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AppSettings />} />
    <Route path="new" element={<AppSettingsUpdate />} />
    <Route path=":id">
      <Route index element={<AppSettingsDetail />} />
      <Route path="edit" element={<AppSettingsUpdate />} />
      <Route path="delete" element={<AppSettingsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AppSettingsRoutes;
