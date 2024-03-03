import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PasswordResets from './password-resets';
import PasswordResetsDetail from './password-resets-detail';
import PasswordResetsUpdate from './password-resets-update';
import PasswordResetsDeleteDialog from './password-resets-delete-dialog';

const PasswordResetsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PasswordResets />} />
    <Route path="new" element={<PasswordResetsUpdate />} />
    <Route path=":id">
      <Route index element={<PasswordResetsDetail />} />
      <Route path="edit" element={<PasswordResetsUpdate />} />
      <Route path="delete" element={<PasswordResetsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PasswordResetsRoutes;
