import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscribedOrderDelivery from './subscribed-order-delivery';
import SubscribedOrderDeliveryDetail from './subscribed-order-delivery-detail';
import SubscribedOrderDeliveryUpdate from './subscribed-order-delivery-update';
import SubscribedOrderDeliveryDeleteDialog from './subscribed-order-delivery-delete-dialog';

const SubscribedOrderDeliveryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscribedOrderDelivery />} />
    <Route path="new" element={<SubscribedOrderDeliveryUpdate />} />
    <Route path=":id">
      <Route index element={<SubscribedOrderDeliveryDetail />} />
      <Route path="edit" element={<SubscribedOrderDeliveryUpdate />} />
      <Route path="delete" element={<SubscribedOrderDeliveryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscribedOrderDeliveryRoutes;
