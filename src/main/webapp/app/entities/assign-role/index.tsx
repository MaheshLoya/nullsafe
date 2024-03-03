import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AssignRole from './assign-role';
import AssignRoleDetail from './assign-role-detail';
import AssignRoleUpdate from './assign-role-update';
import AssignRoleDeleteDialog from './assign-role-delete-dialog';

const AssignRoleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AssignRole />} />
    <Route path="new" element={<AssignRoleUpdate />} />
    <Route path=":id">
      <Route index element={<AssignRoleDetail />} />
      <Route path="edit" element={<AssignRoleUpdate />} />
      <Route path="delete" element={<AssignRoleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssignRoleRoutes;
