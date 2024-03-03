import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Migrations from './migrations';
import MigrationsDetail from './migrations-detail';
import MigrationsUpdate from './migrations-update';
import MigrationsDeleteDialog from './migrations-delete-dialog';

const MigrationsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Migrations />} />
    <Route path="new" element={<MigrationsUpdate />} />
    <Route path=":id">
      <Route index element={<MigrationsDetail />} />
      <Route path="edit" element={<MigrationsUpdate />} />
      <Route path="delete" element={<MigrationsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MigrationsRoutes;
