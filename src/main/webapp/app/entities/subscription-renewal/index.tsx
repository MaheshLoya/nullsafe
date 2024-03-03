import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscriptionRenewal from './subscription-renewal';
import SubscriptionRenewalDetail from './subscription-renewal-detail';
import SubscriptionRenewalUpdate from './subscription-renewal-update';
import SubscriptionRenewalDeleteDialog from './subscription-renewal-delete-dialog';

const SubscriptionRenewalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscriptionRenewal />} />
    <Route path="new" element={<SubscriptionRenewalUpdate />} />
    <Route path=":id">
      <Route index element={<SubscriptionRenewalDetail />} />
      <Route path="edit" element={<SubscriptionRenewalUpdate />} />
      <Route path="delete" element={<SubscriptionRenewalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscriptionRenewalRoutes;
