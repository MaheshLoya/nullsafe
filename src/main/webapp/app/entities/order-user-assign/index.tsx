import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderUserAssign from './order-user-assign';
import OrderUserAssignDetail from './order-user-assign-detail';
import OrderUserAssignUpdate from './order-user-assign-update';
import OrderUserAssignDeleteDialog from './order-user-assign-delete-dialog';

const OrderUserAssignRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderUserAssign />} />
    <Route path="new" element={<OrderUserAssignUpdate />} />
    <Route path=":id">
      <Route index element={<OrderUserAssignDetail />} />
      <Route path="edit" element={<OrderUserAssignUpdate />} />
      <Route path="delete" element={<OrderUserAssignDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderUserAssignRoutes;
