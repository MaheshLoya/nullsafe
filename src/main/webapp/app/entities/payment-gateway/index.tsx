import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentGateway from './payment-gateway';
import PaymentGatewayDetail from './payment-gateway-detail';
import PaymentGatewayUpdate from './payment-gateway-update';
import PaymentGatewayDeleteDialog from './payment-gateway-delete-dialog';

const PaymentGatewayRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentGateway />} />
    <Route path="new" element={<PaymentGatewayUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentGatewayDetail />} />
      <Route path="edit" element={<PaymentGatewayUpdate />} />
      <Route path="delete" element={<PaymentGatewayDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentGatewayRoutes;
