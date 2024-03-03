import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BannerImage from './banner-image';
import BannerImageDetail from './banner-image-detail';
import BannerImageUpdate from './banner-image-update';
import BannerImageDeleteDialog from './banner-image-delete-dialog';

const BannerImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BannerImage />} />
    <Route path="new" element={<BannerImageUpdate />} />
    <Route path=":id">
      <Route index element={<BannerImageDetail />} />
      <Route path="edit" element={<BannerImageUpdate />} />
      <Route path="delete" element={<BannerImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BannerImageRoutes;
