import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InvoiceSetting from './invoice-setting';
import InvoiceSettingDetail from './invoice-setting-detail';
import InvoiceSettingUpdate from './invoice-setting-update';
import InvoiceSettingDeleteDialog from './invoice-setting-delete-dialog';

const InvoiceSettingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InvoiceSetting />} />
    <Route path="new" element={<InvoiceSettingUpdate />} />
    <Route path=":id">
      <Route index element={<InvoiceSettingDetail />} />
      <Route path="edit" element={<InvoiceSettingUpdate />} />
      <Route path="delete" element={<InvoiceSettingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InvoiceSettingRoutes;
