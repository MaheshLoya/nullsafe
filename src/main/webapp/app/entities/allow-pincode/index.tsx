import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllowPincode from './allow-pincode';
import AllowPincodeDetail from './allow-pincode-detail';
import AllowPincodeUpdate from './allow-pincode-update';
import AllowPincodeDeleteDialog from './allow-pincode-delete-dialog';

const AllowPincodeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AllowPincode />} />
    <Route path="new" element={<AllowPincodeUpdate />} />
    <Route path=":id">
      <Route index element={<AllowPincodeDetail />} />
      <Route path="edit" element={<AllowPincodeUpdate />} />
      <Route path="delete" element={<AllowPincodeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AllowPincodeRoutes;
