import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscribedOrders from './subscribed-orders';
import SubscribedOrdersDetail from './subscribed-orders-detail';
import SubscribedOrdersUpdate from './subscribed-orders-update';
import SubscribedOrdersDeleteDialog from './subscribed-orders-delete-dialog';

const SubscribedOrdersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscribedOrders />} />
    <Route path="new" element={<SubscribedOrdersUpdate />} />
    <Route path=":id">
      <Route index element={<SubscribedOrdersDetail />} />
      <Route path="edit" element={<SubscribedOrdersUpdate />} />
      <Route path="delete" element={<SubscribedOrdersDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscribedOrdersRoutes;
