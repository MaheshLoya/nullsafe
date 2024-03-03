import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AvailableDeliveryLocation from './available-delivery-location';
import AvailableDeliveryLocationDetail from './available-delivery-location-detail';
import AvailableDeliveryLocationUpdate from './available-delivery-location-update';
import AvailableDeliveryLocationDeleteDialog from './available-delivery-location-delete-dialog';

const AvailableDeliveryLocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AvailableDeliveryLocation />} />
    <Route path="new" element={<AvailableDeliveryLocationUpdate />} />
    <Route path=":id">
      <Route index element={<AvailableDeliveryLocationDetail />} />
      <Route path="edit" element={<AvailableDeliveryLocationUpdate />} />
      <Route path="delete" element={<AvailableDeliveryLocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AvailableDeliveryLocationRoutes;
