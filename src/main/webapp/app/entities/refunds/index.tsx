import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Refunds from './refunds';
import RefundsDetail from './refunds-detail';
import RefundsUpdate from './refunds-update';
import RefundsDeleteDialog from './refunds-delete-dialog';

const RefundsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Refunds />} />
    <Route path="new" element={<RefundsUpdate />} />
    <Route path=":id">
      <Route index element={<RefundsDetail />} />
      <Route path="edit" element={<RefundsUpdate />} />
      <Route path="delete" element={<RefundsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RefundsRoutes;
